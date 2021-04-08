package com.example.covenantsermons.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.repository.ImageRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class PlayerService : MediaBrowserServiceCompat(),KoinComponent{
//class PlayerService : MediaBrowserServiceCompat(), KoinScopeComponent{
    //only one Exoplayer created
    private val exoPlayer: ExoPlayer by inject()
    //only one PlaybackPreparer created
    private val playbackPreparer: PlaybackPreparer by inject()
    //private val imageViewModel: ImageViewModel by inject()
    private val imageRepository: ImageRepository by inject()
 //   private val imageViewModel: ImageViewModel by viewModel<ImageViewModel>()
//    private val nowPlayingChannelId: String = "NOW_PLAYING"
//    private val nowPlayingNotificationId: Int = 1
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var mStateBuilder: PlaybackStateCompat.Builder

    private var bigIconBitmap: Bitmap?=null
    private var intentWithFlags:Intent?=null
    private lateinit var pendingIntent: PendingIntent
    private var createPendingIntent=true



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

        // pendingIntent launches mainActivity from notification when clicked
        val sessionActivityPendingIntent =
                packageManager?.getLaunchIntentForPackage(packageName)?.let { mediaSessionIntent ->
                    Timber.i("mediaSessionIntent= $mediaSessionIntent")
//                    val sermonBundle = Bundle()
//                    val sermonParcelable=Sermon()
//                    sermonBundle.putParcelable(SERMON_PODCAST_PARCELABLE,sermonParcelable)
//                    mediaSessionIntent.putExtra(SERMON_PODCAST_BUNDLE,sermonBundle)

//                    PendingIntent.getActivity(this, 1, mediaSessionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    PendingIntent.getActivity(this, 1, mediaSessionIntent, 0)
                }

        mediaSession = MediaSessionCompat(this, "Player Service")
                .apply {
                    setSessionActivity(sessionActivityPendingIntent)
                    isActive = true
                }

        Timber.i("mediaSession= $mediaSession")

        //used to get same player
        sessionToken = mediaSession.sessionToken

        notificationManager = NotificationManagerCompat.from(this)

        if (shouldCreateChannel(notificationManager)) {
            Timber.i("shouldCreateChannel returns true will create channel")
            createPlayChannel(notificationManager)
        }

//        val intentWithFlags = Intent(this@PlayerService, MainActivity::class.java).apply{
//            this.addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
//            this.addCategory("PlayerService")
//        }
//
//        pendingIntent = PendingIntent.getActivity(
//                this@PlayerService,
//                0,
//                intentWithFlags,
//                PendingIntent.FLAG_UPDATE_CURRENT
//
//        )
//
//        val notification = NotificationCompat.Builder(this)
//                .setContentIntent(pendingIntent).build()
//
//        notificationManager.notify(NOW_PLAYING_NOTIFICATION_ID, notification)
//        create a pending intent pass the intent into it add pending int to notificationmanager
//        val activity = PendingIntent.getActivity(this, 0, intent, 0)




        sessionToken?.let { mediaSessionToken ->
            Timber.i("playerNotification manager being created")
            val playerNotificationManager = PlayerNotificationManager(this, NOW_PLAYING_CHANNEL_ID,
                    NOW_PLAYING_NOTIFICATION_ID,
                    //interface instantiation
                    object : PlayerNotificationManager.MediaDescriptionAdapter {
                        //controller needs sessionToken
                        val controller = MediaControllerCompat(this@PlayerService, mediaSessionToken)

                        override fun getCurrentContentText(mPlayer: Player): String? {
//                            val sermon = mPlayer.currentTag as? Sermon
                            val sermon = mPlayer.currentTag as? Sermon
                            return sermon?.pastorName
                        }

                        override fun getCurrentContentTitle(mPlayer: Player): String {
                            Timber.i("getCurrentContentTitle mPlayer= $mPlayer")
                            Timber.i("getCurrentContentTitle mPlayer.currentTag= ${mPlayer.currentTag}")
                            val sermon = mPlayer.currentTag as? Sermon
                            Timber.i("getCurrentContentTitle called sermon =$sermon")
                            return sermon?.title ?: "sermon title not found"
                        }

                        //TODO no bitmap is shown
                        override fun getCurrentLargeIcon(
                                mPlayer: Player,
                                callback: PlayerNotificationManager.BitmapCallback
                        ): Bitmap? {

//                            return controller.metadata?.description?.iconBitmap

                            Timber.i("getCurrentLargeIcon mPlayer= $mPlayer")
                            Timber.i("getCurrentLargeIcon mPlayer.currentTag= ${mPlayer.currentTag}")
                            if (mPlayer.currentTag == null) {
                                return controller.metadata?.description?.iconBitmap
                            }
                            try {

                                val sermon = mPlayer.currentTag as? Sermon
                                Timber.i("getCurrentLargeIcon sermon= $sermon")


                                val sermonImage = sermon?.image!!
                                Timber.i("sermonImage= $sermonImage")
                                val file: File = File(sermonImage)
                                val b = BitmapFactory.decodeStream(FileInputStream(file))
                                Timber.i("bitmap= $b")
                                //  val bitmap= BitmapFactory.decodeFile(sermonImage)
                                //Timber.i("bitmap= $bitmap")
                                return b

                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                                return controller.metadata?.description?.iconBitmap
                            }
//


//                            val icon = BitmapFactory.decodeResource(this@PlayerService.context.getResources(),
//                                    R.drawable.icon_resource)


//                                                        imageViewModel.currentSermonImage.observe(this@PlayerService, Observer { currentSermonImage ->
//                            })
//                            mPlayer.
//                            Timber.i("mPlayer.getCurrentMediaItem= ${mPlayer.getCurrentMediaItem()}")

                        }

//                        override fun createCurrentContentIntent(player: Player): PendingIntent? {
//                            return null
//                        }


                        override fun createCurrentContentIntent(player: Player): PendingIntent? {
                            Timber.i("createCurrentContentIntent called")

                            if(intentWithFlags==null){
                                intentWithFlags=Intent(this@PlayerService, MainActivity::class.java).apply{
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                }
                            }
                            if (createPendingIntent) {
                                createPendingIntent = false
                                Timber.i("createCurrentContentIntent boolean check called")
//                            val intentWithFlags=Intent(this@PlayerService, MainActivity::class.java)
                                val intentWithFlags = Intent(this@PlayerService, MainActivity::class.java)
//                                        .setFlags(FLAG_ACTIVITY_CLEAR_TOP)
//                                        .setFlags(FLAG_ACTIVITY_CLEAR_TASK)
//                                        .setFlags(FLAG_RECEIVER_REPLACE_PENDING)
//                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addCategory("PlayerService")

                                pendingIntent = PendingIntent.getActivity(
                                        this@PlayerService,
                                        0,
                                        //TODO make sure fragment is correct fragment when directed to MainActivity

//                                    Intent(this@PlayerService, MainActivity::class.java),
//                                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT
//                                    Intent(this@PlayerService, MainActivity::class.java).addCategory("PlayerService"),
                                        intentWithFlags,
                                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_ONE_SHOT
//                                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
//                                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
//                                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or
//                                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
                                )

                                Timber.i("pendingIntent= $pendingIntent")

                            }

                            return pendingIntent

//                            return null
                        }
//                            return PendingIntent.getActivity(
//                                    this@PlayerService,
//                                    0,
//                                    //TODO make sure fragment is correct fragment when directed to MainActivity
//
////                                    Intent(this@PlayerService, MainActivity::class.java),
////                                    PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT
////                                    Intent(this@PlayerService, MainActivity::class.java).addCategory("PlayerService"),
//                                    intentWithFlags,
//                                    PendingIntent.FLAG_UPDATE_CURRENT
////                                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
////                                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
////                                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
////                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or
////                                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
//                            )
//                        }

                    },
                    //interface instantiation
                    object : PlayerNotificationManager.NotificationListener {
                        override fun onNotificationPosted(
                                notificationId: Int,
                                notification: Notification,
                                ongoingNotification: Boolean
                        ) {
                            Timber.i("onNotificationPosted")
                            if (ongoingNotification) {
                                ContextCompat.startForegroundService(
                                        applicationContext,
                                        Intent(applicationContext, this@PlayerService.javaClass)
                                )
                                startForeground(NOW_PLAYING_NOTIFICATION_ID, notification)
                            } else {
                                stopForeground(false)
                            }
                        }

                        override fun onNotificationCancelled(
                                notificationId: Int,
                                dismissedByUser: Boolean
                        ) {
                            Timber.i("onNotificationCancelled called")
                            Timber.i("dismissedByUser= $dismissedByUser")
                            stopForeground(true)
//                            if(dismissedByUser){
//                                stopForeground(true)
//                            }
                        }

                    })

            Timber.i("playerNotificationManager created = $playerNotificationManager")



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
            notificationManager.getNotificationChannel(NOW_PLAYING_CHANNEL_ID) != null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPlayChannel(notificationManager: NotificationManagerCompat) {
        Timber.i("createPlayChannel called")
        val notificationChannel = NotificationChannel(
                NOW_PLAYING_CHANNEL_ID,
                "Now playing channel",
                NotificationManager.IMPORTANCE_LOW
        )
                .apply {
                    description = "Now playing channel"
                }

        notificationManager.createNotificationChannel(notificationChannel)
    }





//    private suspend fun glideCreateBitmap(url: String): Bitmap? =withContext(Dispatchers.IO){
    private suspend fun glideCreateBitmap(url: String): Bitmap? {

    var bitmap: Bitmap? = null

    var futureTargetBitmap: FutureTarget<Bitmap>

    var lmda = null

//        fun foo(lmbda: (bitmap:Bitmap) -> Bitmap){
//            lmbda()
//        }

    //bigIconBitmap=withContext(Dispatchers.IO){ Glide.with(this@PlayerService).asBitmap().load(url).into(object : CustomTarget<Bitmap?>(){
        bigIconBitmap = withContext(Dispatchers.IO) {
            val requestBuilderBitmap: RequestBuilder<Bitmap?> = Glide.with(this@PlayerService).asBitmap().load(url)
            requestBuilderBitmap.into(CustomTargetObject)
            while (CustomTargetObject.bitmapCustom == null) {
                continue
            }
            return@withContext CustomTargetObject.bitmapCustom
        }
        return bigIconBitmap
    }

    fun getPlayerServiceContext()=this



    object CustomTargetObject: CustomTarget<Bitmap?>(){
        var bitmapCustom:Bitmap?=null
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//            TODO("Not yet implemented")
            bitmapCustom=resource

        }

        override fun onLoadCleared(placeholder: Drawable?) {
//            TODO("Not yet implemented")
//            val icon = BitmapFactory.decodeResource(this.getResources(),
//                    R.drawable.icon_resource)
//            bitmapCustom=placeholder
        }

//        fun getBitmap()= bitmapCustom
    }




    companion object {
        const val SERMON_PODCAST_BUNDLE="com.example.covenantsermons.player.bundle"
        const val SERMON_PODCAST_PARCELABLE="com.example.covenantsermons.player.parcelable"
        const val NOW_PLAYING_CHANNEL_ID="NOW_PLAYING"
        const val NOW_PLAYING_NOTIFICATION_ID: Int = 1
    }

}