package com.example.covenantsermons.modelService

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.R
import com.example.covenantsermons.modelDatabase.Sermon
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


//TODO only swiping left or right should stop podcast audio service
//TODO include previous, rewind, play, pause, fast forward and next
//TODO service will be started with startService and then binding therefore need to call stopSelf or
//stopService() from client. In this case when notification is swiped left or right one of the two
//will need to be called.

//Service sends notification and notification has controls media controls user can use to change
//podcast
class ExoplayerNotificationService : Service() {

    private val mContext=this@ExoplayerNotificationService
    private val localBinder: IBinder = LocalBinder();
//    private lateinit var mExoPlayer: SimpleExoPlayer
    private var mExoplayer:SimpleExoPlayer?=null
    private var sermon: Sermon? =Sermon()
    var playerNotificationManager:PlayerNotificationManager?=null

    var calledOnce:Boolean=false

//    val mExoplayer: SimpleExoPlayer by lazy {
//        createPlayer()
//    }

    companion object{
        //get intent and parse intent
        val BUNDLE_KEY="BUNDLE_KEY"
        val SERMON_KEY="SERMON_KEY"

        //notification constants
        val CHANNEL_ID="EXOPLAYER_CHANNEL_ID_100"
    }

    override fun onCreate() {
        super.onCreate();
        //mExoPlayer=createPlayer()

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //releasePlayer();
        var bundle:Bundle? = intent.getBundleExtra(BUNDLE_KEY);
        bundle.let {
            sermon=bundle?.getParcelable<Sermon>(SERMON_KEY)
        }
        createPlayer()
//        mExoplayer
//        if (mExoPlayer == null) {
//            createPlayer()
//        }
        //TODO test if want START_STICKY after killed will restart or START_NOT_STICKY means after killed won't restart
        return START_NOT_STICKY
    }



    override fun onDestroy() {
        releasePlayer()
        super.onDestroy();
    }

    private fun releasePlayer() {
        mExoplayer.let {
            playerNotificationManager?.setPlayer(null)
            mExoplayer?.release()
            mExoplayer = null
        }
    }

//    override fun onBind(intent: Intent?): IBinder? {
//        mBinder
//    }


    override fun onBind(intent: Intent?): IBinder {
        return localBinder;
    }


    fun getPlayerInstance():SimpleExoPlayer?=createPlayer()
//    {
//
////        if(mExoPlayer==null){
////            mExoPlayer=createPlayer()
////        }
////        return mExoPlayer
//        return mExoPlayer
//    }

    private fun createPlayer():SimpleExoPlayer? {
        if(!calledOnce) {
            val trackSelector = DefaultTrackSelector()
            val loadControl = DefaultLoadControl()
            val renderersFactory = DefaultRenderersFactory(mContext)

            mExoplayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl).also {
                it.prepare(prepareMediaSource())
                it.playWhenReady = true
            }
            createNotification()
            calledOnce=true
            return mExoplayer
        }
        return mExoplayer

    }



    fun prepareMediaSource():ExtractorMediaSource {


        val audioFile=sermon?.audioFile
//        var sermon:Sermon = getSermon()
//        val parsedAudioFile=sermon?.audioFile!!.substringAfter("//")
//        val audioArrayString=parsedAudioFile.split("/")
        //val sermonAudioUri:Uri=parsedAudioFile.toUri()
        //var sermonAudioUri:Uri = sermon?.audioFile!!.toUri()
//        val segments=sermonAudioUri.pathSegments
//        val audioUriSegment=segments[segments.size - 1]
//        val audioFile=audioArrayString[audioArrayString.size-1]

        val userAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name))

        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(mContext, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(audioFile))

    }

    fun createNotification() {

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(mContext,
                CHANNEL_ID,
                //this is the title of the notification
                R.string.channel_name,
                R.string.NOTIFICATION_ID,
                DescriptionAdapter(mContext, sermon)).apply {
                    setNotificationListener(createNotificationListener())
                    setPlayer(mExoplayer)
                }

    }
    private fun createNotificationListener():PlayerNotificationManager.NotificationListener {

        return object : PlayerNotificationManager.NotificationListener {

            override fun onNotificationStarted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification);
            }

            override fun onNotificationCancelled(notificationId: Int) {
                mContext.stopSelf();
            }
        }
    }



    inner class LocalBinder : Binder() {
        val service: ExoplayerNotificationService
            get() = this@ExoplayerNotificationService
    }

}





    //TODO getSermons needs to return correct sermon in arraylist
//    fun getSermons()= arrayListOf<Sermon>(Sermon())
//returns new sermon each time called doesn't matter only for testing
//    fun getSermon()= Sermon()


//creates sermon only first time
//val lazyValue: Sermon by lazy {
//    Sermon()
//}


class DescriptionAdapter(val mContext: Context, val sermon: Sermon?) : MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): String {
        //int for current playing index
//        val window = player.currentWindowIndex
//        return getTitle(window)
        return sermon?.title!!
    }


    override fun getCurrentContentText(player: Player): String {
//        val window = player.currentWindowIndex
//        return BundleJUnitUtils.getDescription(window)
        return sermon?.pastorName!!
    }

    override fun getCurrentLargeIcon(player: Player,
                                     callback: BitmapCallback): Bitmap? {
//        val window = player.currentWindowIndex
//        val largeIcon: Bitmap = getLargeIcon(window)
//        if (largeIcon == null && getLargeIconUri(window) != null) {
//            // load bitmap async
//            loadBitmap(getLargeIconUri(window), callback)
//            return getPlaceholderBitmap()
//        }
        return convertUrlToBitmap(sermon?.image!!)
    }


    override fun createCurrentContentIntent(player: Player): PendingIntent? {
//        val window = player.currentWindowIndex

        val notificationIntent = Intent(mContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(mContext,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //return createPendingIntent(window)
        return pendingIntent
    }
}


//image url convert to bitmap
//    file:///home/david/Downloads/cross.png

fun convertUrlToBitmap(imageUrl: String)= run {
//    val parsedImageUrl=imageUrl.substringAfter("//")
//    val url: URL =URL(parsedImageUrl)
//    val segments=url.path.split("/")
//    val segments=parsedImageUrl.split("/")
//    val imageUrlSegment=segments[segments.size - 1]
    val bitmap = BitmapFactory.decodeFile(imageUrl)
    //val bitmap=BitmapFactory.decodeStream(url.openConnection().getInputStream())
    bitmap
}

//val source = ImageDecoder.createSource(activity!!.contentResolver, Uri.parse(event.localPhotoUri))
//                    val bitmap = ImageDecoder.decodeBitmap(source)






















//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        Bundle b = intent . getBundleExtra (AppConstants.BUNDLE_KEY);
//        if (b != null) {
//            item = b.getParcelable(AppConstants.ITEM_KEY);
//        }
//        startPlayer();
//        return START_STICKY;
//    }

//    private void startPlayer()
//
//{
//        final Context context = this;
//        Uri uri = Uri . parse (item.getUrl());
//        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector ());
//        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
//                context,
//                Util.getUserAgent(context, getString(R.string.app_name))
//        );
//        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(uri);
//        player.prepare(mediaSource);
//        player.setPlayWhenReady(true);
//        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(context,
//                AppConstants.PLAYBACK_CHANNEL_ID,
//                R.string.playback_channel_name,
//                AppConstants.PLAYBACK_NOTIFICATION_ID,
//                new PlayerNotificationManager . MediaDescriptionAdapter () {
//                    @Override
//                    public String getCurrentContentTitle(Player player) {
//                        return item.getTitle();
//                    }
//
//                    @Nullable
//                    @Override
//                    public PendingIntent createCurrentContentIntent(Player player) {
//                        Intent intent = new Intent(context, PlayerActivity.class);
//                        Bundle serviceBundle = new Bundle();
//                        serviceBundle.putParcelable(AppConstants.ITEM_KEY, item);
//                        intent.putExtra(AppConstants.BUNDLE_KEY, serviceBundle);
//                        return PendingIntent.getActivity(
//                                context,
//                                0,
//                                intent,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                    }
//
//                    @Nullable
//                    @Override
//                    public String getCurrentContentText(Player player) {
//                        return item.getSummary();
//                    }
//
//                    @Nullable
//                    @Override
//                    public Bitmap getCurrentLargeIcon(
//                            Player player,
//                            PlayerNotificationManager.BitmapCallback callback
//                    ) {
//                        return item.getBitmap();
//                    }
//                }
//        );
//        playerNotificationManager.setNotificationListener(new PlayerNotificationManager . NotificationListener () {
//            @Override
//            public void onNotificationStarted(int notificationId, Notification notification) {
//                startForeground(notificationId, notification);
//            }
//
//            @Override
//            public void onNotificationCancelled(int notificationId) {
//                stopSelf();
//            }
//        });
//        playerNotificationManager.setPlayer(player);
//    }
//
//
//    inner class LocalBinder : Binder() {
//        val service: ExoplayerNotificationService
//            get() = this@ExoplayerNotificationService
//    }
//}






















































































//    private lateinit var mPlayer: SimpleExoPlayer
//    private lateinit var dataSourceFactory: DataSource.Factory
//    private lateinit var playerNotificationManager: PlayerNotificationManager
//
//
//    companion object{
//        const val NOTIFICATION_ID = 1
//        const val CHANNEL_ID = R.string.CHANNEL_ID
//        const val CHANNEL_DESCRIPTION=R.string.CHANNEL_DESCRIPTION
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        val context = this
////        mPlayer = SimpleExoPlayer.Builder(this).build()
//        ExoPlayerImpl().initializePlayer();
//        mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
//        playerNotificationManager.setPlayer(player);
//        // Create a data source factory.
//        dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(context, "app-name"))
//        mPlayer.prepare(getListOfMediaSource())
//        mPlayer.playWhenReady = true
//
//
////	createWithNotificationChannel​(Context context, String channelId, int channelName, int channelDescription,
////	int notificationId, PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter,
////	PlayerNotificationManager.NotificationListener notificationListener
//        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
//            this,
//            CHANNEL_ID.toString(),
//            NOTIFICATION_ID,
//            NOTIFICATION_ID,
//            object : PlayerNotificationManager.MediaDescriptionAdapter {
//
//
//                override fun createCurrentContentIntent(player: Player): PendingIntent? {
//                    // return pending intent
//                    val intent = Intent(context, PlayPodcastActivity::class.java);
//                    return PendingIntent.getActivity(
//                        context, 0, intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                    )
//                }
//
//                //pass description here
//                override fun getCurrentContentText(player: Player): String? {
//                    return "Description"
//                }
//
//                //pass title (mostly playing audio name)
//                override fun getCurrentContentTitle(player: Player): String {
//                    return "Title"
//                }
//
//                // pass image as bitmap
//                override fun getCurrentLargeIcon(
//                    player: Player,
//                    callback: PlayerNotificationManager.BitmapCallback
//                ): Bitmap? {
//                    return null
//                }
//            },
//            object : PlayerNotificationManager.NotificationListener {
//
//                override fun onNotificationPosted(
//                    notificationId: Int,
//                    notification: Notification,
//                    onGoing: Boolean
//                ) {
//
//                    startForeground(notificationId, notification)
//
//                }
//
//                override fun onNotificationCancelled(
//                    notificationId: Int,
//                    dismissedByUser: Boolean
//                ) {
//                    stopSelf()
//                }
//
//            }
//        )
//        //attach player to playerNotificationManager
//        playerNotificationManager.setPlayer(mPlayer)
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }
//
//    // concatenatingMediaSource to pass media as a list,
//    // so that we can easily prev, next
//    private fun getListOfMediaSource(): ConcatenatingMediaSource {
//        val mediaUrlList = ArrayList<String>()
//        mediaUrlList.add("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
//        mediaUrlList.add("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")
//        mediaUrlList.add("http://d3rlna7iyyu8wu.cloudfront.net/skip_armstrong/skip_armstrong_stereo_subs.m3u8")
//        mediaUrlList.add("https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8")
//        mediaUrlList.add("https://multiplatform-f.akamaihd.net/i/multi/will/bunny/big_buck_bunny_,640x360_400,640x360_700,640x360_1000,950x540_1500,.f4v.csmil/master.m3u8")
//
//        val concatenatingMediaSource = ConcatenatingMediaSource()
//        for (mediaUrl in mediaUrlList) {
//            concatenatingMediaSource.addMediaSource(buildMediaSource(mediaUrl))
//        }
//
//        return concatenatingMediaSource
//
//    }
//
//    //build media source to player
//    private fun buildMediaSource(videoUrl: String): HlsMediaSource? {
//        val uri = Uri.parse(videoUrl)
//        // Create a HLS media source pointing to a playlist uri.
//        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
//    }
//
////    @MainThread
////    private fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
////        return ContextCompat.getDrawable(context, drawableId)?.let {
////            val drawable = DrawableCompat.wrap(it).mutate()
////
////            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
////            val canvas = Canvas(bitmap)
////            drawable.setBounds(0, 0, canvas.width, canvas.height)
////            drawable.draw(canvas)
////
////            bitmap
////        }
////    }
//
//    // detach player
//    override fun onDestroy() {
//        playerNotificationManager.setPlayer(null)
//        mPlayer.release()
//        super.onDestroy()
//    }
//
//    //removes service when user swipes left or right on notification
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        super.onTaskRemoved(rootIntent)
//        playerNotificationManager.setPlayer(null);
//        mPlayer.release();
//        mPlayer = null;
//        stopSelf()
//    }



//}