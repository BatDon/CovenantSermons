package com.example.covenantsermons.workers

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.covenantsermons.extensions.dateToUnderscoredDate
import com.example.covenantsermons.extensions.deserializeFromJson
import com.example.covenantsermons.extensions.httpsRefToStorageRef
import com.example.covenantsermons.extensions.pathToName
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.repository.Repository
import com.example.covenantsermons.viewmodel.DownloadViewModel
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext

//TODO Create interface for both workers to extend from
//class AudioWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params), Repository {
class AudioWorker(context: Context, params: WorkerParameters) : Worker(context, params), Repository, CoroutineScope, KoinComponent {
//class AudioRepository(val mContext: Context): Repository, CoroutineScope, KoinComponent {
    private lateinit var mContext: Context

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    init{
        Timber.i("AudioWorker created")
        mContext=context
    }

    override fun doWork(): Result {
        Timber.i("doWork called")
        val appContext = applicationContext

        val serializedSermon=inputData.getString(DownloadViewModel.KEY_SERMON_SERIALIZED)
        val sermon=serializedSermon?.deserializeFromJson()

        var fileName:String

        return try {
            var outputfileName: Data = workDataOf(AudioWorker.KEY_AUDIO to "audioFile")
            Timber.i("in try block")
            if (sermon==null) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            else{
                var audioFile: String?=null
                runBlocking(coroutineContext) {
                    val audioStorageReference = sermon.audioFile?.httpsRefToStorageRef()

                    //getAudioFile()
                    //val audioStorageReference=sermon.audioFile as StorageReference.getRef
                    audioStorageReference.let {
                        if (it != null) {
                            audioFile=saveToFile(sermon, it, mContext)
                        }
//                   fileName= it.name
                        Timber.i("fileName= $audioStorageReference")

                    }
                }
                Timber.i("outputFileName assigned to $audioFile")
                outputfileName = workDataOf(AudioWorker.KEY_AUDIO to audioFile)

            }
//            val outputfileName: Data = workDataOf(AudioWorker.KEY_AUDIO to "someString")

            Result.success(outputfileName)
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error downloading image")
            Result.failure()
        }
    }



    private suspend fun saveToFile(sermon: Sermon, audioStorageReference:StorageReference, mContext: Context):String? = withContext(Dispatchers.IO){
        var audioPath: String?=createAudioPath(sermon)
        val audioFileName:String?=sermon.audioFile?.pathToName()
        val dir= File(mContext.filesDir, audioPath!!)
        if (!dir.exists()) {
            Timber.i("making dir imagePath= $audioPath")
            dir.mkdir()
        }
        else{
            Timber.i("dir already exists imagePath= $audioPath")
        }
        try {

            //val audioFile:File? = audioFileName.let{File.createTempFile(it!!, "mp3")}
            val audioFile = File(dir, audioFileName!!)
            //val writer = FileWriter(bitmapFile)
            val outputStream= FileOutputStream(audioFile)
//            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //writer.append(bitmap)
            //writer.flush()
            //writer.close()
            outputStream.flush()
            outputStream.close()
            Timber.i("finished try saveBitmapToInternalStorage")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Timber.i("returning imagePath")
        return@withContext audioPath
    }


//    private suspend fun saveToFile(sermon: Sermon, audioStorageReference:StorageReference):String? = withContext(Dispatchers.IO){
//        var audioPath: String?=createAudioPath(sermon)
//
//        val audioFile:File? = audioPath.let{File.createTempFile(it!!, "mp3")}
//
//        return@withContext if(audioFile!=null){
//            audioPath
//        }
//        else{
//            null
//        }
//
//    }





//    fun getAudioFile(url: String): Bitmap?{
//        Timber.i("getSermonImage called url= $url")
//        var bitmap: Bitmap?=null
////        launch{
//        runBlocking(coroutineContext){
//            bitmap=call(url)
//            currentSermonImage.value = bitmap!!
////        coroutineScope.launch {
////            call()
//        }
//        //return currentSermonImage
//        Timber.i("return bitmap called bitmap=$bitmap")
//        return bitmap
//    }

    private fun createAudioPath(sermon: Sermon):String{
        val dateUnderscored= sermon.dateToUnderscoredDate()
//        val dateUnderscored=sermon.date?.replace("/","_")
        val audioName=sermon.audioFile?.pathToName()?.split(".")!![0]
        return dateUnderscored+"_"+sermon.title+"_"+"bitmapImage_"+audioName
    }

    companion object{
        const val KEY_AUDIO="KEY_AUDIO"
    }

    fun cancelAudioJob(){
        coroutineContext.job.cancel()
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun cancelJob() {
        Timber.i("cancelJob called")
        if (job.isActive) {
            Timber.i("job is active coroutine still fetching image")
            Timber.i("about to cancel job")
            job.cancel()
        }
    }

}