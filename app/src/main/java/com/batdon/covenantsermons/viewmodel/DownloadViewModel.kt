package com.batdon.covenantsermons.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.batdon.covenantsermons.extensions.serializeToJson
import com.batdon.covenantsermons.modelClass.Sermon
import com.batdon.covenantsermons.workers.AudioWorker
import com.batdon.covenantsermons.workers.ImageWorker
import timber.log.Timber

//class DownloadViewModel(application: Application) : AndroidViewModel(application) {
class DownloadViewModel(private val workManager: WorkManager) : ViewModel(){
    var imageFileLocation: String?=null
    var audioFileLocation: String?=null

    var sermonArrayList: ArrayList<Sermon> = ArrayList<Sermon>()
//    private val _currentSermonDownloading = MutableLiveData<ArrayList<Sermon>>()
//    val currentSermonDownloading: LiveData<ArrayList<Sermon>> = _currentSermonDownloading
//class DownloadViewModel() : ViewModel(), KoinComponent {

//    init{
//        WorkManager.getInstance(mContext)
//        if(workManager==null){
//            Timber.i("workManager equals null")
//        }
//        else{
//            Timber.i("workManager does not equal null")
//        }
//    }




   // var outputWorkInfos: LiveData<(MutableList<WorkInfo>)> = workManager.getWorkInfosByTagLiveData(IMAGE_TAG)

    //TODO can tag as list or seperate

    val outputImageWorkInfos: LiveData<List<WorkInfo>>
    val outputAudioWorkInfos: LiveData<List<WorkInfo>>
    internal val outputWorkInfos: LiveData<List<WorkInfo>>

//    val audioImageWorkInfo: LiveData<List<WorkInfo>>
//    val outputImageAudioWorkInfos: LiveData<List<WorkInfo>>
//        get()=_outputImageAudioWorkInfos
//    private val _outputImageAudioWorkInfos: MutableLiveData<List<WorkInfo>> = MutableLiveData<List<WorkInfo>>()


    init {
        //TODO add both IMAGE_WORK AND AUDIO_WORK

        outputImageWorkInfos= workManager.getWorkInfosByTagLiveData(IMAGE_WORK)
        outputAudioWorkInfos= workManager.getWorkInfosByTagLiveData(AUDIO_WORK)
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(IMAGE_AUDIO_DOWNLOAD_WORK)
//        audioImageWorkInfo=workManager.getWorkInfosForUniqueWork()
//        var imageWorkInfo: WorkInfo?= outputImageWorkInfos.value?.get(0)
//        var audioWorkInfo: WorkInfo?= outputAudioWorkInfos.value?.get(0)

//        _outputImageAudioWorkInfos=MutableLiveData<List<WorkInfo>>()
     //   _outputImageAudioWorkInfos.postValue(listOf(imageWorkInfo!!,audioWorkInfo!!))

//        _outputWorkInfos.value.
//        add(workManager.getWorkInfosByTagLiveData(AUDIO_WORK))
//        val workInfoListLiveData: MutableLiveData<List<WorkInfo>>=workManager.getWorkInfosByTagLiveData(IMAGE_WORK)
//        workInfoListLiveData.value.add(workManager.getWorkInfosByTagLiveData(AUDIO_WORK))
//        outputWorkInfos = workManager.getWorkInfosByTagLiveData(IMAGE_WORK)
//        workInfoListLiveData=_outputWorkInfos
    }

//    fun setUpCurrentSermon(sermonArrayList: ArrayList<Sermon>){
//        Timber.i("sermon= $sermonArrayList")
//        _currentSermonDownloading.value?.clear()
//        _currentSermonDownloading.value=sermonArrayList
//        Timber.i("_currentSermonDownloading.value= ${_currentSermonDownloading.value}")
//    }


    //Monitors state of worker
//    var outputWorkInfos: LiveData<(MutableList<WorkInfo>)> = workManager.getWorkInfosByTagLiveData(IMAGE_TAG)

//    override fun startWork() {
//        workManager.beginUniqueWork(
//                IMAGE_AUDIO_DOWNLOAD_WORK,
//                ExistingWorkPolicy.APPEND,
//            listOf(OneTimeWorkRequest.from(ImageWorker::class.java),
//                    OneTimeWorkRequest.from(AudioWorker::class.java))
//        ).enqueue()
//    }


    fun startWork(sermon: Sermon) {
        val workRequests=buildWorkRequests(sermon)
        //labels chain of work requests
        Timber.i("startWork called $sermon")
        workManager.beginUniqueWork(
                IMAGE_AUDIO_DOWNLOAD_WORK,
                ExistingWorkPolicy.REPLACE,
//                ExistingWorkPolicy.APPEND_OR_REPLACE,
                workRequests
//                listOf(createImageWorkRequest(sermon),
//                        createAudioWorkRequest(sermon))
        ).enqueue()
    }

    private fun buildWorkRequests(sermon: Sermon): MutableList<OneTimeWorkRequest>{
        val workRequests = mutableListOf<OneTimeWorkRequest>()
        workRequests.add(createImageWorkRequest(sermon))
        workRequests.add(createAudioWorkRequest(sermon))
        return workRequests
    }
    private fun createImageWorkRequest(sermon: Sermon):OneTimeWorkRequest {
        Timber.i("createImageWorkRequest sermon= $sermon")
        return OneTimeWorkRequest.Builder(ImageWorker::class.java)
                .addTag(IMAGE_WORK)
                .setConstraints(createDownloadConstraints())
                .setInputData(createDataObject(sermon))
//                .setInputData(workDataOf(KEY_SERMON_SERIALIZED to "sermonString"))
                .build()
    }

    private fun createAudioWorkRequest(sermon: Sermon):OneTimeWorkRequest {
//        OneTimeWorkRequestBuilder<AudioWorker>()
        Timber.i("createAudioWorkRequest called sermon= $sermon")
        return OneTimeWorkRequest.Builder(AudioWorker::class.java)
                .addTag(AUDIO_WORK)
                .setConstraints(createDownloadConstraints())
//                .setInputData(workDataOf(KEY_SERMON_SERIALIZED to "sermonString"))
                .setInputData(createDataObject(sermon))
                .build()
    }

//    private fun createImageWorkRequest(sermon: Sermon)=
//         OneTimeWorkRequest.Builder(ImageWorker::class.java)
//                .addTag(IMAGE_WORK)
//                .setConstraints(createDownloadConstraints())
//                .setInputData(createDataObject(sermon))
//                .build()

//    private fun createAudioWorkRequest(sermon: Sermon)=
////        OneTimeWorkRequestBuilder<AudioWorker>()
//            Timber.i("createAudioWorkRequest called sermon= $sermon")
//            OneTimeWorkRequest.Builder(AudioWorker::class.java)
//                .addTag(AUDIO_WORK)
//                .setConstraints(createDownloadConstraints())
//                .setInputData(createDataObject(sermon))
//                .build()

    private fun createDownloadConstraints()=
       Constraints.Builder()
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()


    fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_AUDIO_DOWNLOAD_WORK)
    }

//    fun createDataObject (date: String,  imageUri: String, audioUri: String){
//        val mData: Data = workDataOf(
//                KEY_SERMON_DATE to date,
//                KEY_IMAGE_URI to imageUri,
//                KEY_AUDIO_URI to audioUri)
//    }

    private fun createDataObject (sermon: Sermon):Data{
        val serializedSermon=sermon.serializeToJson()
        return workDataOf(KEY_SERMON_SERIALIZED to serializedSermon)
    }

    companion object{
        const val IMAGE_AUDIO_DOWNLOAD_WORK="IMAGE_AUDIO_DOWNLOAD_WORK"
        const val IMAGE_AUDIO_OUTPUT=" IMAGE_AUDIO_OUTPUT"
        const val IMAGE_DOWNLOAD_WORK="IMAGE_DOWNLOAD_WORK"
        const val KEY_SERMON_SERIALIZED="KEY_SERMON_SERIALIZED"
        const val KEY_SERMON_DATE = "KEY_SERMON_DATE"
        const val KEY_IMAGE_URI="KEY_IMAGE_URI"
        const val KEY_AUDIO_URI="KEY_AUDIO_URI"
        const val IMAGE_WORK="IMAGE_WORK"
        const val AUDIO_WORK="AUDIO_WORK"
    }
}

//class DownloadViewModelFactory(private val workManager: WorkManager) : ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(DownloadViewModel::class.java)) {
//            return DownloadViewModel(workManager) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}