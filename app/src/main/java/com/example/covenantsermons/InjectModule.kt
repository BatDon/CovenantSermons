package com.example.covenantsermons

import android.content.Context
import android.content.SharedPreferences
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
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
        }
    }

    single(named("userAgent")) {
        //application name and library version
        val userAgent = Util.getUserAgent(androidContext(), "Covenant Sermons")
        userAgent
    }

    single {
        //TODO create PlaybackPreparer class
        PlaybackPreparer(get())
    }

}