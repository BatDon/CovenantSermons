package com.example.covenantsermons.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covenantsermons.modelDatabase.Sermon

class PodcastListViewModel(
       // private val sermonDatabase: SermonDatabase

//        private val coroutineContext: CoroutineContext,
//        private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
//    private val _podcasts = MutableLiveData<List<Sermon>>()
//    val podcasts: LiveData<List<Sermon>> = _podcasts
    private val _podcasts = MutableLiveData<ArrayList<Sermon>>()
    val podcasts: LiveData<ArrayList<Sermon>> = _podcasts
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errors = MutableLiveData<String>()
    val errors: LiveData<String> = _errors

    fun getPodcastsFromDatabase(){
        //val sermonDatabase=SermonDatabase()
        //Timber.i("getPodcastsFromDatabase called sermonDatabase= $sermonDatabase")
        //sermonDatabase.getPodcastsFromDatabase()
    }

//    fun getPodcasts() {
//        viewModelScope.launch(coroutineContext) {
//            _isLoading.value = true
//            try {
//                val podcasts = withContext(coroutineDispatcher) {
////                    podcastRepository.getTopPodcasts()
//                }
////                _podcasts.value = podcasts
//            } catch (ex: Exception) {
//                _errors.value = ex.localizedMessage
//            }
//
//            _isLoading.value = false
//        }
//    }

    fun setPodcasts(arrayListSermon: ArrayList<Sermon>){
        _podcasts.value?.clear()
        _podcasts.value=arrayListSermon

    }

//    fun transformLiveData():ArrayList<Sermon> {
////        val sermonArrayList=ArrayList<Sermon>()
////        val sermon:Sermon = podcasts.map{sermon ->
////            sermonArrayList.add(sermon)
////        }
////        return sermonArrayList
//        val sermonAdrrayList:ArrayList<Sermon>? =podcasts.value
//        Timber.i("sermonAdrrayList $sermonAdrrayList")
//
////        val sermonArrayList:ArrayList<Sermon> =liveDataToSermonArrayList(podcasts){ list -> list }
////        Timber.i("transform live data sermonArrayList size ${sermonArrayList.size}")
////        return sermonArrayList
//
//        val sermonArrayList:ArrayList<Sermon> =liveDataToSermonArrayList(podcasts){ list -> list }
//        Timber.i("transform live data sermonArrayList size ${sermonArrayList.size}")
//        return sermonArrayList
////        return liveDataToSermonArrayList(podcasts) { list -> list }
//    }

//        Timber.i("transformLiveData podcasts size= ${podcasts.value?.size}")
//
//        val liveSermonList = Transformations.map(podcasts) { list ->
//            Timber.i("list in transformation size= ${list.size}")
//
//            list?.forEach {
//                val tempSermon = Sermon()
//                tempSermon.audioFile = it.audioFile
//                tempSermon.title = it.title
//                tempSermon.pastorName = it.pastorName
//                tempSermon.date = it.date
//                tempSermon.duration = it.duration
//                tempSermon.image = it.image
//                sermonArrayList.add(tempSermon)
//            }
//
//        }
//        //TODO not mapping correctly sermonArrayList size is 0
//        Timber.i("transformedLiveData sermonArrayList.size= ${sermonArrayList.size}")
//        return sermonArrayList
//    }

}