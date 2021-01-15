package com.example.covenantsermons.exoplayerAccess

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.covenantsermons.exoplayer.ExoplayerImpl
import com.example.covenantsermons.exoplayer.ExoplayerInterface
import com.google.android.exoplayer2.ui.PlayerView
import java.lang.ref.WeakReference

interface PodcastViewContract {

    interface ExoplayerAccesserInterface {

        fun deactivate()

//        fun getExoplayer(): MediaPlayer
        fun getExoplayer(): ExoplayerInterface

        fun play(url: String)

        fun releaseExoplayer()

        fun setMediaSessionState(isActive: Boolean)
    }

    interface View
}

class PodcastViewer(videoViewView: PodcastViewContract.View) : PodcastViewContract.ExoplayerAccesserInterface {

    private val view = WeakReference(videoViewView)

    private val mExoMediaPlayer = ExoplayerImpl()

    override fun deactivate() {
    }

    override fun getExoplayer() = mExoMediaPlayer

    override fun play(url: String) = mExoMediaPlayer.play(url)

    override fun releaseExoplayer() = mExoMediaPlayer.releaseExoplayer()

    override fun setMediaSessionState(isActive: Boolean) {
        mExoMediaPlayer.setMediaSessionState(isActive)
    }
}

class PodcastViewActivity : AppCompatActivity(), PodcastViewContract.View {

    companion object {
        const val VIDEO_URL_EXTRA = "video_url_extra"
    }

    private lateinit var exoPlayerPodcastView: PlayerView

    private lateinit var exoplayerAccesserInterface: PodcastViewContract.ExoplayerAccesserInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_podcast_view)
        init()
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            exoplayerAccesserInterface.releaseExoplayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            exoplayerAccesserInterface.releaseExoplayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayerAccesserInterface.deactivate()
        exoplayerAccesserInterface.setMediaSessionState(false)
    }

    private fun init() {
        exoplayerAccesserInterface = PodcastViewer(this)

        val podcastUrl: String? = intent.getStringExtra(VIDEO_URL_EXTRA)

        //exoPlayerPodcastView = findViewById(R.id.exoplayer_podcast_view)

        exoPlayerPodcastView.player = exoplayerAccesserInterface.getExoplayer().getExoplayerImpl(this)

        //make sure podcastUrl is not null before playing podcast
        podcastUrl?.let { exoplayerAccesserInterface.play(podcastUrl)}

    }
}