package com.example.covenantsermons.player

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covenantsermons.modelClass.Sermon
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File


class PlayerViewModel(
        private val exoPlayer: ExoPlayer,
        private val dataSourceFactory: DataSource.Factory,
//        private val dataSourceFactory: FileDataSource.Factory,
        private val mediaSessionConnection: MediaSessionConnection,
        private val playbackPreparer: PlaybackPreparer
) : ViewModel() {
    private val _isConnected=MutableLiveData<Boolean>()
    private val _currentlyPlaying = MutableLiveData<Sermon>()
    private val _playlist = mutableListOf<Sermon>()
    val playlist
        get() = _playlist.subList(1, _playlist.size)
    val currentlyPlaying: LiveData<Sermon> = _currentlyPlaying
    private var previousIndex=0
    private var currentIndex = 0

    var audioDir: File? =null

   // private var storageRoot:String=""
    //use coroutine to get image
    val _currentSermonImage=MutableLiveData<Bitmap>()
    val currentSermonImage
        get() = _currentSermonImage

    private val listener = object : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                Timber.i("playBackState= STATE_ENDED")
//                currentIndex += 1
//                _currentlyPlaying.value = _playlist[currentIndex]
            }
            else if(playbackState == Player.STATE_READY){
                Timber.i("playbackState= STATE_READY")
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
        override fun onPositionDiscontinuity(reason: Int) {
            Timber.i("onPositionDiscontinuity reason= $reason")
//            if (reason==Player.DISCONTINUITY_REASON_PERIOD_TRANSITION){
//                Timber.i("onPositionDiscontinuity DISCONTINUITY_REASON_PERIOD_TRANSITION triggered")
//                currentIndex += 1
//                _currentlyPlaying.value = _playlist[currentIndex]
//            }
            //TODO error if press previous button, because it increases value of current index
            if (reason==Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT){
                Timber.i("_playlist= $_playlist")
                Timber.i("_playlist size= ${_playlist.size}")
                currentIndex=exoPlayer.currentWindowIndex
                Timber.i("currentIndex= $currentIndex")
                _currentlyPlaying.value = _playlist[currentIndex]
                Timber.i("onPositionDiscontinuity DISCONTINUITY_REASON_SEEK_ADJUSTMENT triggered")
//                if (currentIndex==previousIndex && previousIndex > 0){
//                    Timber.i("going backward in tracks")
//                    Timber.i("previousIndex= $previousIndex")
//                    Timber.i("currentIndex= $currentIndex")
//                    currentIndex -= 1
//                    _currentlyPlaying.value = _playlist[currentIndex]
//                    Timber.i("previousIndex= $previousIndex")
//                    Timber.i("currentIndex decrease by 1= $currentIndex")
//                }
//                else{
//                    Timber.i("going forward in tracks")
//                    Timber.i("previousIndex= $previousIndex")
//                    Timber.i("currentIndex increase by 1= $currentIndex")
//                    currentIndex += 1
//                    _currentlyPlaying.value = _playlist[currentIndex]
//                    Timber.i("previousIndex= $previousIndex")
//                    Timber.i("currentIndex increase by 1= $currentIndex")
//                }
//                getPreviousTrackIndex()

            }

        }


//        override fun onPlaybackStateChanged(){
//
//        }

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
        Timber.i("play called currentSermon=$currentSermon")
        val newPlaylist = sermonList.filter { it.audioFile != currentSermon.audioFile }
        createPlaylist(currentSermon, newPlaylist)
    }
//    fun play(currentSermon: Sermon) {
//        val newPlaylist = _playlist.filter { it.audioFile != currentSermon.audioFile }
//        createPlaylist(currentSermon, newPlaylist)
//    }

    fun playFromPlaylist(next: Sermon) {
        val current = _playlist[currentIndex]
        Timber.i("next.audioFile= $next.audioFile")
        val newPlaylist =
            _playlist.filter { it.audioFile != next.audioFile && it.audioFile != current.audioFile }
        createPlaylist(next, newPlaylist)
    }

    fun createPlaylist(
            next: Sermon?,
            newPlaylist: List<Sermon>
    ) {
        //runBlocking { getMediaSessionConnection() }
//        Timber.i("runBlocking coroutine finished")
//        mediaSessionConnection


        _playlist.clear()
        if(next!=null){
            _playlist.add(next)
        }
        _playlist.addAll(newPlaylist)
        Timber.i("newPlaylist size= ${newPlaylist.size}")
        Timber.i("next= $next")
        Timber.i("newPlaylist= $newPlaylist")
        Timber.i("1 _playlist size= ${_playlist.size}")


//        val mediaSources = (listOf(next) + playlist).map {
//            ProgressiveMediaSource.Factory(dataSourceFactory)
//                .setTag(it)
//                .createMediaSource(Uri.parse(it.audioFile))
//        }.toTypedArray()
        var playListNext:List<Sermon>? =null
        playListNext = if(next !=null) {
            listOf(next) + playlist
        }
        else {
            playlist
        }
            val mediaSources = (playListNext).map {
//            ProgressiveMediaSource.Factory(it.audioFile?.let { sermonAudioFile -> createDataSource(sermonAudioFile) })
                ProgressiveMediaSource.Factory(FileDataSource.Factory())
                        .setTag(it)
                        .createMediaSource(Uri.fromFile(File(it.audioFile!!)))
//                    .createMediaSource(Uri.parse(it.audioFile))
            }.toTypedArray()
            Timber.i("mediaSources size= ${mediaSources.size}")
            Timber.i("_playlist size= ${_playlist.size}")
            playbackPreparer.mediaSource = ConcatenatingMediaSource(*mediaSources)
            Timber.i("before mediaSessionConnection.transportControls")
        Timber.i("before mediaSessionConnection.transportControls.playFromMediaId((next.audioFile), null)")
        Timber.i("after mediaSessionConnection.transportControls.playFromMediaId(next.audioFile, null)")


        if(next !=null){
            mediaSessionConnection.transportControls.playFromMediaId((next.audioFile), null)
            currentIndex=0
            _currentlyPlaying.value = next!!
        }

        Timber.i("after mediaSessionConnection.transportControls")
//        currentIndex = 0
//        _currentlyPlaying.value = next
    }

    fun emptyPlayList(){
        //mediaSessionConnection.transportControls.pause()
        mediaSessionConnection.transportControls.stop()
        _currentlyPlaying.value=Sermon()
        _playlist.clear()

    }



    private fun createDataSource(audioFile:String):DataSource.Factory{
        return DataSource.Factory {
            val dataSpec=DataSpec(Uri.parse(audioFile))
            val fileDataSource=FileDataSource()
            fileDataSource.open(dataSpec)
            fileDataSource
        }
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