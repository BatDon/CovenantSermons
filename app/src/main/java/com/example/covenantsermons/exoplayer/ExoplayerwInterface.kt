package com.example.covenantsermons.exoplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.covenantsermons.R
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


interface ExoplayerInterface {

    fun play(url: String)

    fun getExoplayerImpl(context: Context): ExoPlayer

    fun releaseExoplayer()

    fun setMediaSessionState(isActive: Boolean)
}

class ExoplayerImpl : ExoplayerInterface {

    companion object {
        private const val TAG = "MediaPlayerTag"
    }

    private lateinit var mExoPlayer: ExoPlayer
    private lateinit var context: Context
    private lateinit var mMediaSession: MediaSessionCompat
    private lateinit var mPlaybackStateBuilder: PlaybackStateCompat.Builder

    override fun play(url: String) {

        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))

        val extractorMediaSource = ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, userAgent))
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(url))

        mExoPlayer.prepare(extractorMediaSource)

        mExoPlayer.playWhenReady = true
    }

    override fun getExoplayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        initializeMediaSession()
        return mExoPlayer
    }

    override fun releaseExoplayer() {
        mExoPlayer.stop()
        mExoPlayer.release()
    }

    override fun setMediaSessionState(isActive: Boolean) {
        mMediaSession.isActive = isActive
    }

    private fun initializePlayer() {

        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl)
    }

    private fun initializeMediaSession() {
        mMediaSession = MediaSessionCompat(context, TAG)
//        mMediaSession.setFlags(
//                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
//                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
//        )
        mMediaSession.setMediaButtonReceiver(null)

        mPlaybackStateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY or
                                PlaybackStateCompat.ACTION_PAUSE or
                                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                                PlaybackStateCompat.ACTION_FAST_FORWARD or
                                PlaybackStateCompat.ACTION_REWIND
                )

        mMediaSession.setPlaybackState(mPlaybackStateBuilder.build())

        mMediaSession.setCallback(SessionCallback())

        mMediaSession.isActive = true
    }

    private inner class SessionCallback : MediaSessionCompat.Callback() {

        private val SEEK_BAR_MILLIS = 30000

        override fun onPlay() {
            mExoPlayer.playWhenReady = true
        }

        override fun onPause() {
            mExoPlayer.playWhenReady = false
        }

        override fun onRewind() {
            mExoPlayer.seekTo(mExoPlayer.currentPosition - SEEK_BAR_MILLIS)
        }

        override fun onFastForward() {
            mExoPlayer.seekTo(mExoPlayer.currentPosition + SEEK_BAR_MILLIS)
        }
    }
}