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
        _podcasts.value=arrayListSermon
    }
}