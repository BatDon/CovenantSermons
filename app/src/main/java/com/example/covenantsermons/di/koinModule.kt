package com.example.covenantsermons.di

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import com.example.covenantsermons.player.MediaSessionConnection
import com.example.covenantsermons.player.PlaybackPreparer
import com.example.covenantsermons.player.PlayerService
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


//module to use same single copy koin uses this module to create single copy for all instances
val koinModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("shared-prefs", Context.MODE_PRIVATE)
    }

    single<ExoPlayer> {
        SimpleExoPlayer.Builder(androidContext()).build().apply {
            this.setAudioAttributes(
                    AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC)
                            .setUsage(C.USAGE_MEDIA).build(), true
            )
            this.setHandleAudioBecomingNoisy(true)

            this.addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                }
            })
        }
    }


    single(named("userAgent")) {
        //application name and library version
        val userAgent = Util.getUserAgent(androidContext(), "Covenant Sermons")
        userAgent
    }

    single<DataSource.Factory> {
        DefaultHttpDataSourceFactory(
                get<String>(named("userAgent")),
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true
        )
    }

    single {
        PlaybackPreparer(get())
    }

    single {
        MediaSessionConnection(
                androidContext(),
                ComponentName(androidContext(), PlayerService::class.java),

        )
    }

}
