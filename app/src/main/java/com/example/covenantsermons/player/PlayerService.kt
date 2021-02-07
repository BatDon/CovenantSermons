package com.example.covenantsermons.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.modelDatabase.Sermon
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.koin.android.ext.android.inject
import timber.log.Timber

class PlayerService : MediaBrowserServiceCompat() {
    //only one Exoplayer created
    private val exoPlayer: ExoPlayer by inject()
    //only one PlaybackPreparer created
    private val playbackPreparer: PlaybackPreparer by inject()
    private val nowPlayingChannelId: String = "NOW_PLAYING"
    private val nowPlayingNotificationId: Int = 1
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManagerCompat

    //supports devices such as Google Assistant, Bluetooth headsets and media buttons
    override fun onLoadChildren(
            parentId: String,
            result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //supports devices such as Google Assistant, Bluetooth headsets and media buttons
    override fun onGetRoot(
            clientPackageName: String,
            clientUid: Int,
            rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot("root", rootHints)
    }

    override fun onCreate() {
        super.onCreate()

        Timber.i("onCreate called in PlayerService")

        val sessionActivityPendingIntent =
                packageManager?.getLaunchIntentForPackage(packageName)?.let { mediaSessionIntent ->
                    PendingIntent.getActivity(this, 1, mediaSessionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

        mediaSession = MediaSessionCompat(this, "Player Service")
                .apply {
                    setSessionActivity(sessionActivityPendingIntent)
                    isActive = true
                }

        //used to get same player
        sessionToken = mediaSession.sessionToken

        notificationManager = NotificationManagerCompat.from(this)

        if (shouldCreateChannel(notificationManager)) {
            createPlayChannel(notificationManager)
        }

        sessionToken?.let {mediaSessionToken ->
            val playerNotificationManager = PlayerNotificationManager(this, nowPlayingChannelId,
                    nowPlayingNotificationId,
                    //interface instantiation
                    object : PlayerNotificationManager.MediaDescriptionAdapter {
                        //controller needs sessionToken
                        val controller = MediaControllerCompat(this@PlayerService, mediaSessionToken)

                        override fun getCurrentContentText(mPlayer: Player): String? {
                            val sermon = mPlayer.currentTag as? Sermon
                            return sermon?.pastorName
                        }

                        override fun getCurrentContentTitle(mPlayer: Player): String {
                            val sermon = mPlayer.currentTag as? Sermon
                            return sermon?.title ?: "sermon title not found"
                        }

                        //TODO no bitmap is shown
                        override fun getCurrentLargeIcon(
                                player: Player,
                                callback: PlayerNotificationManager.BitmapCallback
                        ): Bitmap? {
                            return controller.metadata?.description?.iconBitmap
                        }

                        override fun createCurrentContentIntent(player: Player): PendingIntent? {
                            return PendingIntent.getActivity(
                                    this@PlayerService,
                                    0,
                                //TODO make sure fragment is correct fragment when directed to MainActivity
                                    Intent(this@PlayerService, MainActivity::class.java),
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        }

                    },
                //interface instantiation
                object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                        notificationId: Int,
                        notification: Notification,
                        ongoingNotification: Boolean
                ) {
                    if (ongoingNotification) {
                        ContextCompat.startForegroundService(
                                applicationContext,
                                Intent(applicationContext, this@PlayerService.javaClass)
                        )
                        startForeground(nowPlayingNotificationId, notification)
                    } else {
                        stopForeground(false)
                    }
                }
            })

            //playerNotificationManager needs access to exoPlayer single instance and sessionToken
            //to keep it in sync
            playerNotificationManager.setPlayer(exoPlayer)
            playerNotificationManager.setMediaSessionToken(mediaSessionToken)
        }

        MediaSessionConnector(mediaSession).also {
            it.setPlayer(exoPlayer)
            //create single Instance of playbackPreparer in Inject
            it.setPlaybackPreparer(playbackPreparer)
        }
    }

    //only need one channel can change to singleton class
    private fun shouldCreateChannel(notificationManager: NotificationManagerCompat) =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !channelExists(
                    notificationManager
            )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun channelExists(notificationManager: NotificationManagerCompat) =
            notificationManager.getNotificationChannel(nowPlayingChannelId) != null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPlayChannel(notificationManager: NotificationManagerCompat) {
        val notificationChannel = NotificationChannel(
                nowPlayingChannelId,
                "Now playing channel",
                NotificationManager.IMPORTANCE_LOW
        )
                .apply {
                    description = "Now playing channel"
                }

        notificationManager.createNotificationChannel(notificationChannel)
    }
}