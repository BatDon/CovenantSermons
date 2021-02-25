package com.example.covenantsermons.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
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
import com.example.covenantsermons.ImageRepository
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.modelClass.Sermon
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import timber.log.Timber


class PlayerService : MediaBrowserServiceCompat(),KoinComponent{
//class PlayerService : MediaBrowserServiceCompat(), KoinScopeComponent{
    //only one Exoplayer created
    private val exoPlayer: ExoPlayer by inject()
    //only one PlaybackPreparer created
    private val playbackPreparer: PlaybackPreparer by inject()
    //private val imageViewModel: ImageViewModel by inject()
    private val imageRepository: ImageRepository by inject()
 //   private val imageViewModel: ImageViewModel by viewModel<ImageViewModel>()
    private val nowPlayingChannelId: String = "NOW_PLAYING"
    private val nowPlayingNotificationId: Int = 1
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var mStateBuilder: PlaybackStateCompat.Builder

    private var bigIconBitmap: Bitmap?=null




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
//                    val sermonBundle = Bundle()
//                    val sermonParcelable=Sermon()
//                    sermonBundle.putParcelable(SERMON_PODCAST_PARCELABLE,sermonParcelable)
//                    mediaSessionIntent.putExtra(SERMON_PODCAST_BUNDLE,sermonBundle)

                    PendingIntent.getActivity(this, 1, mediaSessionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

        mediaSession = MediaSessionCompat(this, "Player Service")
                .apply {
                    setSessionActivity(sessionActivityPendingIntent)
                    isActive = true
                    //added below 2/6/2021
                    setFlags(
                            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
                    )
                    mStateBuilder = PlaybackStateCompat.Builder()
                            .setActions(PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE
                                    or PlaybackStateCompat.ACTION_PAUSE)
                    setPlaybackState(mStateBuilder.build())
                    //added above 2/6/2021
                }

        //used to get same player
        sessionToken = mediaSession.sessionToken

        notificationManager = NotificationManagerCompat.from(this)

        if (shouldCreateChannel(notificationManager)) {
            createPlayChannel(notificationManager)
        }

        sessionToken?.let { mediaSessionToken ->
            Timber.i("playerNotification manager being created")
            val playerNotificationManager = PlayerNotificationManager(this, nowPlayingChannelId,
                    nowPlayingNotificationId,
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
                            val sermon = mPlayer.currentTag as? Sermon
                            return sermon?.title ?: "sermon title not found"
                        }

                        //TODO no bitmap is shown
                        override fun getCurrentLargeIcon(
                                mPlayer: Player,
                                callback: PlayerNotificationManager.BitmapCallback
                        ): Bitmap? {
                            Timber.i("getCurrentLargeIcon called")
//                            imageViewModel.currentSermonImage.observe(this@PlayerService, Observer { currentSermonImage ->
//                            })

                            val sermon = mPlayer.currentTag as? Sermon
                            val bitmap=sermon?.image?.let { imageRepository.getSermonImage(it) }

                            //val sermonBitmap= imageViewModel.currentSermonImage.value

//                            val sermon = mPlayer.currentTag as? Sermon
//                            sermon.image
//                            runBlocking { sermon?.image?.let { glideCreateBitmap(it) } }

//                            var bitmap:Bitmap?=null
//                            GlobalScope.launch(Dispatchers.Main) { sermon?.image?.let { bitmap= glideCreateBitmap(it) } }
//                            Timber.i("bitmap after coroutine is $bitmap")

//                            runBlocking {
//                                val bitmap = GlobalScope.async{ sermon?.image?.let { glideCreateBitmap(it) } }
//                                bitmap.await()
//                            }

//                            runBlocking {
//                                val bitmap:Bitmap = launch(Dispatchers.IO){ sermon?.image?.let { glideCreateBitmap(it) } }
//                                return bitmap
//                            }
//                            var bitmap = null
//
//                            val coroutineScope = CoroutineScope(Dispatchers.Main)
//
//                            GlobalScope.launch(Dispatchers.Main) {
////                                val bitmap:Bitmap? =  sermon?.image?.let { glideCreateBitmap(it) }
//                                val bitmapFromGlide: Bitmap? = sermon?.image?.let { glideCreateBitmap(it) }
//                                // setBitmapForIcon(bitmap)
//
//                                // return@launch bitmap
//
//                            }
                            //bitmapFromGlide

//                            fun setBitmapForIcon(bitmap:Bitmap){
//
//                            }

//                            return bigIconBitmap

//                            return sermonBitmap
                            return bitmap

//                            val bitmap = GlobalScope.async{ sermon?.image?.let { glideCreateBitmap(it) } }
//                            return bitmap.await()

//                            return controller.metadata?.description?.iconBitmap
//                            return Glide.with(this@PlayerService).asBitmap().load(sermon?.image).error(R.drawable.cross)
//                            Glide.with(this@PlayerService).load(sermon?.image).error(R.drawable.cross)
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

  //  suspend fun glideCreateBitmap(url: String): Bitmap? =withContext(Dispatchers.IO) {


//    suspend fun glideCreateBitmap(url: String): Bitmap? =withContext(Dispatchers.IO) {
//
//            var bitmap: Bitmap? = null
//
//            var futureTargetBitmap: FutureTarget<Bitmap>
//
//            Glide.with(this@PlayerService).asBitmap().load(url).into(object : CustomTarget<Bitmap?>(){
//
//
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                    bitmap = resource
//                    Timber.i("onResourceReady bitmap= $bitmap")
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//
//            return@withContext bitmap
//        }




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


//            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                    bigIconBitmap=resource
////                bitmap = resource
//                //callbackReturnBitmap(bitmap)
//                //return bitmap
//               // lmda={bitmap -> return bitmap}
//                Timber.i("onResourceReady bitmap= $bitmap")
//                return@withContext resource
//            }
//
//            override fun onLoadCleared(placeholder: Drawable?) {
//                TODO("Not yet implemented")
//            }

//            fun callbackReturnBitmap(bitmap:Bitmap?){
//                return bitmap
//            }

 //       })

//        while(bigIconBitmap==null){
//            continue
//        }

//        while(bitmap==null){
//            continue
//        }

//        fun callbackReturnBitmap(bitmap:Bitmap){
//
//        }

//        return@withContext bitmap
//    }

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











//    suspend fun glideCreateBitmap(url: String): Bitmap? =withContext(Dispatchers.IO) {
////        var bitmap: Bitmap?=null
////        launch {
//        var bitmap: Bitmap? = null
//
//        Glide.with(this@PlayerService).asBitmap().load(url).into(object : CustomTarget<Bitmap?>() {
//
//            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                bitmap = resource
//                Timber.i("onResourceReady bitmap= $bitmap")
//            }
//
//            override fun onLoadCleared(placeholder: Drawable?) {
//                TODO("Not yet implemented")
//            }
//
//        })

//        return@withContext bitmap
//    }



    companion object {
        const val SERMON_PODCAST_BUNDLE="com.example.covenantsermons.player.bundle"
        const val SERMON_PODCAST_PARCELABLE="com.example.covenantsermons.player.parcelable"
    }

}