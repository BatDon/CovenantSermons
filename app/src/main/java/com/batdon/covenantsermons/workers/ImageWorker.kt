package com.batdon.covenantsermons.workers

import android.content.Context
import android.graphics.Bitmap
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.batdon.covenantsermons.extensions.*
import com.batdon.covenantsermons.modelClass.Sermon
import com.batdon.covenantsermons.repository.ImageRepository
import com.batdon.covenantsermons.viewmodel.DownloadViewModel.Companion.KEY_SERMON_SERIALIZED
import com.google.firebase.storage.StorageReference
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

//class ImageWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params), KoinComponent {
class ImageWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), KoinComponent {

    private lateinit var mContext: Context

    init{
       Timber.i("ImageWorker created")
        mContext=context
    }

    private val imageRepository: ImageRepository by inject()

    override fun doWork(): Result {
        Timber.i("doWork called")
        val appContext = applicationContext

        val serializedSermon=inputData.getString(KEY_SERMON_SERIALIZED)
        val sermon=serializedSermon?.deserializeFromJson()

//        val imageUri = inputData.getString(KEY_IMAGE_URI)

        return try {
            var outputBitmap: Data?=null
            if (sermon==null) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            else{
                val bitmap=sermon.image?.let { imageRepository.getSermonImage(it) }
                var bitmapPath : String? =null
                Timber.i("bitmap using imageRepository= $bitmap")
                bitmap?.let {
                    //saveBitmapToFile(sermon, it)
                    val imageStorageReference = sermon.image?.httpsRefToStorageRef()
                    imageStorageReference.let{imageStorageReference ->
                        bitmapPath= saveBitmapToInternalStorage(sermon, imageStorageReference!!, it,mContext)
                    }

                }
                val outputBitmap= workDataOf(KEY_IMAGE_BITMAP_FILE_PATH to bitmapPath)
                //val outputSermon= workDataOf(KEY_SERMON to sermon)
               // val dataArray= arrayOf<Data>(outputBitmap,outputSermon)
                outputBitmap.let{
                    if (it != null) {
                       return@doWork Result.success(it)
                    }
                }

            }
            return Result.failure()
//            outputBitmap.let{
//                if (it != null) {
//                    Result.success(it)
//                }
//            }
//            Result.success(outputBitmap)


        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error downloading image")
            return Result.failure()
        }

    }

//    private fun saveBitmapToFile(sermon: Sermon, bitmap: Bitmap):String?{
//        return try {
//            Timber.i("saveBitmapToFile bitmap= $bitmap")
//            val filePath=Uri.Builder().appendPath(sermon.date)
//                    .appendPath(sermon.title)
//                    .appendPath(sermon.image?.pathToImageName())
//            Timber.i("saveBitmapToFile path= $filePath")
//            FileOutputStream(filePath.toString()).use { fileOutputStream ->
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
//            }
//            filePath.toString()
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }

   //TODO download from Firebase and save to internal storage check AudioWorker for reference
    private fun saveBitmapToInternalStorage(sermon: Sermon, imageStorageReference: StorageReference,  bitmap: Bitmap?, mContext: Context, ):String {



        val imagePath:String =createImagePath(sermon)
        val imageFileName:String?=sermon.image?.pathToName()
       val fileRoot=File(this@ImageWorker.mContext.filesDir.toString())
       val dir=mContext.createDir(imagePath)
      //  val dir= File(mContext.filesDir, imagePath)
       var imageFile:File?=null
        var bitmapPath:String=""
        if (!dir.exists()) {
            Timber.i("making dir imagePath= $imagePath")
            dir.mkdir()
        }
        else{
            Timber.i("dir already exists imagePath= $imagePath")
        }
        try {
            imageFile = File(dir, imageFileName!!)
            imageStorageReference.getFile(imageFile)
            bitmapPath=imageFile.toString()
            //val writer = FileWriter(bitmapFile)
            val outputStream= FileOutputStream(imageFile)
            //bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            //imageStorageReference.getFile(imageFile)
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
        return bitmapPath
    }

    fun createImagePath(sermon: Sermon):String{
        val dateUnderscored= sermon.dateToUnderscoredDate()
        //val dateUnderscored=sermon.date?.replace("/","_")
        val bitmapName=sermon.image?.pathToName()?.split(".")!![0]
//        val imagePath=mContext.createRootStoragePath()+dateUnderscored+"_"+sermon.title+"_"+"bitmapImage_"+bitmapName
        return dateUnderscored+"_"+sermon.title+"_bitmapImage_"+bitmapName
    }


//    fun createImagePath(sermon: Sermon)=
//            "cat".replace("/","_")
//    val dateUnderscored=sermon.date.replace("/","_")
    //sermon.date.replaceAll("/", "_")+"/"+sermon.title+"/bitmapImage/"+sermon.image?.pathToImageName()

//    fun createImagePath(sermon: Sermon)=
//            "cat".replace("/","_")
//        val dateUnderscored=sermon.date.replace("/","_")
//        //sermon.date.replaceAll("/", "_")+"/"+sermon.title+"/bitmapImage/"+sermon.image?.pathToImageName()


//    fun saveBitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
//        //create a file to write bitmap data
//        var file: File? = null
//        return try {
//            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
//            file.createNewFile()
//
//            //Convert bitmap to byte array
//            val bos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
//            val bitmapdata = bos.toByteArray()
//
//            //write the bytes in file
//            val fos = FileOutputStream(file)
//            fos.write(bitmapdata)
//            fos.flush()
//            fos.close()
//            file
//        } catch (e: Exception) {
//            e.printStackTrace()
//            file // it will return null
//        }
//    }

    companion object{
        const val KEY_IMAGE_BITMAP_FILE_PATH="KEY_IMAGE_BITMAP_FILE_PATH"
        const val KEY_SERMON="KEY_SERMON"
    }

}


