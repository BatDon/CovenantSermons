package com.example.covenantsermons.player

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covenantsermons.modelDatabase.Sermon
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class PlayerViewModel(
        private val exoPlayer: ExoPlayer,
        private val dataSourceFactory: DataSource.Factory,
        private val mediaSessionConnection: MediaSessionConnection,
        private val playbackPreparer: PlaybackPreparer
) : ViewModel() {
    private val _isConnected=MutableLiveData<Boolean>()
    val _currentlyPlaying = MutableLiveData<Sermon>()
    private val _playlist = mutableListOf<Sermon>()
    val playlist
        get() = _playlist.subList(1, _playlist.size)
    val currentlyPlaying: LiveData<Sermon> = _currentlyPlaying
    private var currentIndex = 0

    private val listener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                Timber.i("playBackState= STATE_ENDED")
                currentIndex += 1
                _currentlyPlaying.value = _playlist[currentIndex]

            }
        }
        //TODO create observable here MainActivity observes and changes visibility of play and pause
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Timber.i("onIsPlayingChanged called value= $isPlaying")
//            if(isPlaying){
//                exoPlayer.playWhenReady=true
////                exoPlayer.playbackState
//            }else{
//                exoPlayer.playWhenReady=false
//            }
        }

    }

    init {
        exoPlayer.addListener(listener)
    }

//    fun connectedToService(connected:Boolean){
//        _isConnected.value=connected
//        mediaSessionConnection.transportControls.playFromMediaId(next.audioFile, null)
//    }

    override fun onCleared() {
        exoPlayer.removeListener(listener)
    }

    fun play(currentSermon: Sermon, sermonList: List<Sermon>) {
        val newPlaylist = sermonList.filter { it.audioFile != currentSermon.audioFile }
        createPlaylist(currentSermon, newPlaylist)
    }
//    fun play(currentSermon: Sermon) {
//        val newPlaylist = _playlist.filter { it.audioFile != currentSermon.audioFile }
//        createPlaylist(currentSermon, newPlaylist)
//    }

    fun playFromPlaylist(next: Sermon) {
        val current = _playlist[currentIndex]
        val newPlaylist =
            _playlist.filter { it.audioFile != next.audioFile && it.audioFile != current.audioFile }
        createPlaylist(next, newPlaylist)
    }

    private fun createPlaylist(
        next: Sermon,
        newPlaylist: List<Sermon>
    ) {
        //runBlocking { getMediaSessionConnection() }
//        Timber.i("runBlocking coroutine finished")
//        mediaSessionConnection

        _playlist.clear()
        _playlist.add(next)
        _playlist.addAll(newPlaylist)
        Timber.i("newPlaylist size= ${newPlaylist.size}")
        Timber.i("next= $next")
        Timber.i("1 _playlist size= ${_playlist.size}")

        //TODO review this is correct mediaSources list has next, next and then two more items. next and next are the same
//        val mediaSources = (listOf(next) + _playlist).map {
        val mediaSources = (listOf(next) + playlist).map {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .setTag(it)
                .createMediaSource(Uri.parse(it.audioFile))
        }.toTypedArray()
        Timber.i("mediaSources size= ${mediaSources.size}")
        Timber.i("_playlist size= ${_playlist.size}")
        playbackPreparer.mediaSource = ConcatenatingMediaSource(*mediaSources)
        Timber.i("before mediaSessionConnection.transportControls")
        //runBlocking { getMediaSessionConnection() }
        //Timber.i("runBlocking coroutine finished")
        mediaSessionConnection.transportControls.playFromMediaId(next.audioFile, null)
        currentIndex=0
        _currentlyPlaying.value = next





//        mediaSessionConnection.mediaBrowser
//        if(mediaSessionConnection.mediaBrowser.isConnected){
//            Timber.i("mediaBrowser connected")
//        }
//        else{
//            Timber.i("mediaBrowser not connected")
////            mediaSessionConnection.mediaBrowser.connect()
//        }
//        mediaSessionConnection



     //           mediaSessionConnection.transportControls.playFromMediaId(next.audioFile, null)
        Timber.i("after mediaSessionConnection.transportControls")
//        currentIndex = 0
//        _currentlyPlaying.value = next
    }

//    suspend fun getMediaSessionConnection()=withContext(Dispatchers.IO){
    suspend fun getMediaSessionConnection(){
        Timber.i("getMediaSessionConnection called")
        coroutineScope {
//            launch {  mediaSessionConnection }
            launch{mediaSessionConnection.mediaControllerConnection()}
        }
    }
}
//playWhenReady && playbackState == Player.STATE_READY means Exoplayer is playing