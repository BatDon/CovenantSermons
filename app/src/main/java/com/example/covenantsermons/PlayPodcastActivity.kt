package com.example.covenantsermons

//viewbinding
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.covenantsermons.modelDatabase.Sermon
import com.example.covenantsermons.modelService.ExoplayerNotificationService
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.activity_play_podcast.*
import java.util.*


class PlayerActivity: AppCompatActivity() {

//    @BindView(R.id.ep_video_view)
//    val ep_video_view: PlayerView
//    @BindView(R.id.sermonTitleTV)
//    TextView sermonTitleTV;
//    @BindView(R.id.pastorNameTV)
//    TextView pastorNameTV;


//        val audioFile: String = "",
//    val duration: Int = 0,
//    val image: String = "",
//    val timeStamp: Date = Date(),
//    val title: String = "",
//    val pastorName: String=""

    companion object{
        val BUNDLE_KEY="BUNDLE_KEY"
        val SERMON_KEY="SERMON_KEY"
    }

    private var player: SimpleExoPlayer?=null
    private var audioFile:String?=""
    private var duration:Int?=0
    private var image:String?=""
    private var timeStamp:Date?= Date()
    private var title:String?=""
    private var pastorName:String?=""

    //service variables
    private var serviceIsBound=false
    private var exoplayerNotificationService: ExoplayerNotificationService?=null




    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as ExoplayerNotificationService.LocalBinder
            exoplayerNotificationService = binder.service
            serviceIsBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceIsBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_podcast)
        //sermonTitleTV.text = "cat"
        val bundle: Bundle? = intent.getBundleExtra(BUNDLE_KEY)
        bundle.let{bundle ->
//            bundle!!.getParcelable<Parcelable>(SERMON_KEY)

            val sermon: Sermon?= bundle!!.getParcelable<Sermon>(SERMON_KEY);
            audioFile=sermon?.audioFile
            duration=sermon?.duration
            image=sermon?.image
            timeStamp=sermon?.timeStamp
            title=sermon?.title
            pastorName=sermon?.pastorName
            intent = Intent(this, ExoplayerNotificationService::class.java);
            val serviceBundle =Bundle().apply{
                this.putParcelable(SERMON_KEY, sermon)
            }
            intent.putExtra(BUNDLE_KEY, serviceBundle);

//            Only Android O uses foreground Service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }

    private fun initializePlayer() {
        if (player == null && audioFile?.isEmpty() == false && serviceIsBound) {
            player = exoplayerNotificationService?.getPlayerInstance()
            podcast_player_view.setPlayer(player)
//            player.apply{
//                this.setControllerHideOnTouch(false)
//                this.
//            };
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        initializePlayer();
        setUIValues();
    }

    fun setUIValues() {
        sermonTitleTV.setText(title)
        pastorNameTV.setText(pastorName)
        //TODO set image into player
        //player.setImage()
//        GlideApp.with(this)
//                .load(mImage)
//                .placeholder(R.color.colorPrimary)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(mIvThumb);
    }


    override fun onStop() {
        unbindService(mConnection);
        serviceIsBound = false;
        releasePlayer();
        super.onStop();
    }

    fun releasePlayer() {
        player.let{
            it!!.release()
            player =null
        }
//        if (player != null) {
//            player!!.release();
//            player = null;
//        }
    }



}


























//class PlayPodcastActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//    }
//
//    //TODO bind to service
//    override fun onStart() {
//        super.onStart()
//    }
//
//    //TODO unbind from service
//    override fun onStop() {
//        super.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//}

////TODO get bitmap from url
//
////try {
////        URL url = new URL("http://....");
////        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
////    } catch(IOException e) {
////        System.out.println(e);
////    }

