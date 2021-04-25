package com.batdon.covenantsermons.di

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.work.WorkManager
import com.batdon.covenantsermons.database.SermonEntityDatabase
import com.batdon.covenantsermons.player.MediaSessionConnection
import com.batdon.covenantsermons.player.PlaybackPreparer
import com.batdon.covenantsermons.player.PlayerService
import com.batdon.covenantsermons.repository.SermonRepository
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


//module to use same single copy koin uses this module to create single copy for all instances
val koinModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("shared-prefs", Context.MODE_PRIVATE)
    }

    single<ExoPlayer> {
        SimpleExoPlayer.Builder(androidContext(), get<RenderersFactory>()).build().apply {
            this.setAudioAttributes(
                    AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC)
                            .setUsage(C.USAGE_MEDIA).build(), true
            )
            this.setHandleAudioBecomingNoisy(true)
        }
    }

    single<RenderersFactory> {
        val defaultRenderersFactory: DefaultRenderersFactory = DefaultRenderersFactory(androidContext(), null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
        defaultRenderersFactory
    }

//    single<ExoPlayer> {
//        SimpleExoPlayer.Builder(androidContext()).build().apply {
//            this.setAudioAttributes(
//                    AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC)
//                            .setUsage(C.USAGE_MEDIA).build(), true
//            )
//            this.setHandleAudioBecomingNoisy(true)

//            this.addListener(object : Player.EventListener {
//                override fun onIsPlayingChanged(isPlaying: Boolean) {
//                    val layout: XmlResourceParser =androidContext().resources.getLayout(R.layout.exo_playback_control_view)
//                    layout.idAttribute.
//                }
//            })
      //  }
 //   }


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

//    single<DataSource.Factory>{
//        DataSource.Factory(
//            override fun createDataSource(): DataSource {
//                //            TODO("Not yet implemented")
//                return FileDataSource()
//            }
//        )
//    }

//    single<FileDataSource.Factory>()
//    single<DataSource.Factory>{DataSource.Factory(
//            createDataSource():DataSource {
//        return FileDataSource()
//    })
//    }

    single {
        PlaybackPreparer(get())
    }

    single {
        MediaSessionConnection(
                androidContext(),
                ComponentName(androidContext(), PlayerService::class.java)
        )
    }

    //SermonEntityDatabase
    single{
        Room.databaseBuilder(androidApplication(), SermonEntityDatabase::class.java, "sermon-database")
                .build()
    }

    //sermonDao
    single { get<SermonEntityDatabase>().sermonDao() }

//    single{
//        SermonRoomDatabase.getDatabase(get(), get())
//    }

    //SermonRepository
    single{
        SermonRepository(get<SermonEntityDatabase>().sermonDao())
    }

    //WorkManager
    single{
        WorkManager.getInstance(androidApplication())
    }


}

