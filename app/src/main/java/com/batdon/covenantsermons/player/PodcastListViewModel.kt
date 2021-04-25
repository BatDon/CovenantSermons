package com.batdon.covenantsermons.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batdon.covenantsermons.modelClass.Sermon


class PodcastListViewModel(
       // private val sermonDatabase: SermonDatabase

//        private val coroutineContext: CoroutineContext,
//        private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
//    private val _podcasts = MutableLiveData<List<Sermon>>()
//    val podcasts: LiveData<List<Sermon>> = _podcasts
    private val _podcasts = MutableLiveData<ArrayList<Sermon>>()
    val podcasts: LiveData<ArrayList<Sermon>> = _podcasts
    private val _podcastsWithDownloaded = MutableLiveData<ArrayList<Sermon>?>()
    val podcastsWithDownloaded: LiveData<ArrayList<Sermon>?> = _podcasts
    private val _podcastsDownloaded = MutableLiveData<ArrayList<Sermon>?>()
    val podcastsDownloaded: LiveData<ArrayList<Sermon>?> = _podcastsDownloaded
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errors = MutableLiveData<String>()
    val errors: LiveData<String> = _errors

    var currentSermonForRoom:Sermon?=null


    fun setPodcasts(arrayListSermon: ArrayList<Sermon>){
        _podcasts.value?.clear()
        _podcasts.value=arrayListSermon
    }

//    fun setDownloadedPodcasts(arrayListSermon: ArrayList<Sermon>){
//        _podcastsWithDownloaded .value?.clear()
//        _podcastsWithDownloaded .value=arrayListSermon
//    }

    fun setDownloadedPodcasts(arrayListSermon: ArrayList<Sermon>?){
        _podcastsDownloaded.value?.clear()
        _podcastsDownloaded.value=arrayListSermon
    }

    fun getDownloadedPodcasts()= podcastsDownloaded.value



}