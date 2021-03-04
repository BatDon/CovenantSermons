package com.example.covenantsermons.workers

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.covenantsermons.extensions.deserializeFromJson
import com.example.covenantsermons.extensions.httpsRefToStorageRef
import com.example.covenantsermons.viewmodel.DownloadViewModel
import timber.log.Timber

//TODO Create interface for both workers to extend from
class AudioWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    init{
        Timber.i("AudioWorker created")
    }

    override fun doWork(): Result {
        Timber.i("doWork called")
        val appContext = applicationContext

        val serializedSermon=inputData.getString(DownloadViewModel.KEY_SERMON_SERIALIZED)
        val sermon=serializedSermon?.deserializeFromJson()

        var fileName:String

        return try {
            Timber.i("in try block")
            if (sermon==null) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            else{
                val audioStorageReference=sermon.audioFile?.httpsRefToStorageRef()
                //val audioStorageReference=sermon.audioFile as StorageReference.getRef
                    audioStorageReference.let{
//                   fileName= it.name
                        Timber.i("fileName= $audioStorageReference")
                }

            }
            val outputfileName: Data = workDataOf(AudioWorker.KEY_AUDIO to "someString")

            Result.success(outputfileName)
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error downloading image")
            Result.failure()
        }
    }

    companion object{
        const val KEY_AUDIO="KEY_AUDIO"
    }

}