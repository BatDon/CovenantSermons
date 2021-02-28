package com.example.covenantsermons.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.covenantsermons.ImageRepository
import com.example.covenantsermons.extensions.serializeToJson
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.workers.AudioWorker
import com.example.covenantsermons.workers.ImageWorker
import org.koin.core.KoinComponent
import org.koin.core.inject

//class DownloadViewModel(application: Application) : AndroidViewModel(application) {
class DownloadViewModel(private val workManager: WorkManager) : ViewModel(), BaseDownloadViewModel, KoinComponent {



    //Monitors state of worker
    override var outputWorkInfos =workManager.getWorkInfosByTagLiveData(IMAGE_TAG)

//    override fun startWork() {
//        workManager.beginUniqueWork(
//                IMAGE_AUDIO_DOWNLOAD_WORK,
//                ExistingWorkPolicy.APPEND,
//            listOf(OneTimeWorkRequest.from(ImageWorker::class.java),
//                    OneTimeWorkRequest.from(AudioWorker::class.java))
//        ).enqueue()
//    }


    override fun startWork() {
        workManager.beginUniqueWork(
                IMAGE_AUDIO_DOWNLOAD_WORK,
                ExistingWorkPolicy.APPEND,
                listOf(createImageWorkRequest(),
                        createAudioWorkRequest())
        ).enqueue()
    }


    private fun createImageWorkRequest()=
        OneTimeWorkRequestBuilder<ImageWorker>()
                .setConstraints(createDownloadConstraints())
                .setInputData(createDataObject())
                .build()

    private fun createAudioWorkRequest()=
        OneTimeWorkRequestBuilder<AudioWorker>()
                .setConstraints(createDownloadConstraints())
                .setInputData(createDataObject())
                .build()

    private fun createDownloadConstraints()=
       Constraints.Builder()
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()


    override fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_AUDIO_DOWNLOAD_WORK)
    }

//    fun createDataObject (date: String,  imageUri: String, audioUri: String){
//        val mData: Data = workDataOf(
//                KEY_SERMON_DATE to date,
//                KEY_IMAGE_URI to imageUri,
//                KEY_AUDIO_URI to audioUri)
//    }

    fun createDataObject (sermon: Sermon):Data{
        val serializedSermon=sermon.serializeToJson()
        return workDataOf(KEY_SERMON_SERIALIZED to serializedSermon)
    }

    companion object{
        const val IMAGE_AUDIO_DOWNLOAD_WORK="IMAGE_AUDIO_DOWNLOAD_WORK"
        const val IMAGE_DOWNLOAD_WORK="IMAGE_DOWNLOAD_WORK"
        const val KEY_SERMON_SERIALIZED="KEY_SERMON_SERIALIZED"
        const val KEY_SERMON_DATE = "KEY_SERMON_DATE"
        const val KEY_IMAGE_URI="KEY_IMAGE_URI"
        const val KEY_AUDIO_URI="KEY_AUDIO_URI"
    }
}

class DownloadViewModelFactory(private val workManager: WorkManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadViewModel::class.java)) {
            return DownloadViewModel(workManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}