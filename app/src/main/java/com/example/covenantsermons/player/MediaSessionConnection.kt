package com.example.covenantsermons.player

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MediaSessionConnection(context: Context, serviceComponent: ComponentName) {

    private lateinit var mediaController: MediaControllerCompat
    val transportControls: MediaControllerCompat.TransportControls get() = mediaController.transportControls

    init{
        Timber.i("MediaSessionConnection created")
        runBlocking{ // this: CoroutineScope
            launch { // launch a new coroutine in the scope of runBlocking
                //delay(1000L)
                Timber.i("in launch of coroutine")
            }
            Timber.i("outside launch scope of coroutines")
        }
        Timber.i("after coroutine")


    }

    val mediaBrowser =
            MediaBrowserCompat(
                    context,
                    serviceComponent,
                    MediaControllerCallback(context),
                    null
            ).apply {
                Timber.i("MediaSessionConnection before connect called")
                connect()
                Timber.i("MediaSessionConnection after connect called")
            }



    inner class MediaControllerCallback(val context: Context) :
            MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            Timber.i("onConnected called")
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken)
//            playerViewModel.connectedToService(true)
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            Timber.i("onConnect failed")
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            Timber.i("connection suspended")
        }
    }
}



