package com.batdon.covenantsermons.player

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import timber.log.Timber

class PlaybackPreparer(val mExoPlayer: ExoPlayer) : MediaSessionConnector.PlaybackPreparer {
    var mediaSource: MediaSource? = null

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) {
    }

    override fun onCommand(
        player: Player,
        controlDispatcher: ControlDispatcher,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ): Boolean {
        return false
    }

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_REWIND or
                PlaybackStateCompat.ACTION_FAST_FORWARD or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
    }

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        // mExoPlayer set and ready to play
        Timber.i("onPrepareFromMediaId called")
        mediaSource?.let { mediaSource ->
            mExoPlayer.prepare(mediaSource)
            mExoPlayer.playWhenReady = true
            }
//        val controlDispatcher=object : ControlDispatcher{
//            override fun dispatchSetPlayWhenReady(player: Player, playWhenReady: Boolean): Boolean {
//                return true
//            }
//
//            override fun dispatchSeekTo(player: Player, windowIndex: Int, positionMs: Long): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun dispatchSetRepeatMode(player: Player, repeatMode: Int): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun dispatchSetShuffleModeEnabled(player: Player, shuffleModeEnabled: Boolean): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun dispatchStop(player: Player, reset: Boolean): Boolean {
//                TODO("Not yet implemented")
//            }
//        }
    }

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) {
    }

    //TODO decide if play as soon as prepared
    override fun onPrepare(playWhenReady: Boolean) {
    }

}