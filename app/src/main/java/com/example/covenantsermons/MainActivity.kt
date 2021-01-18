package com.example.covenantsermons

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.covenantsermons.modelDatabase.Sermon
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        //TODO uncomment after done testing used to fill recyclerview
//        var sermonArrayList=ArrayList<Sermon>()
        //DatabaseListenerClass(this@MainActivity)
        getPodcastsFromDatabase(this@MainActivity)
//        var sermonArrayList=getPodcastsFromDatabase()

//        if(sermonArrayList.isEmpty()){
//            return
//        }

//        val sermon=Sermon("gs://covenantpodcast-4c1ec.appspot.com/audio_files/krewella-come-get-it.mp3",
//        63,"gs://covenantpodcast-4c1ec.appspot.com/images/jesus.jpeg","pastorName",
//                Date(),"sermonTitle1")

//        val bundle=Bundle().apply {
//            putParcelable(SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//        }
//
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(BUNDLE_KEY,bundle)
//        }
//        startActivity(intent)
    }

//    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//        val bundle=Bundle().apply {
//            putParcelable(SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//        }
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(BUNDLE_KEY,bundle)
//        }
//        startActivity(intent)
//    }

    //
    override fun onStart() {
        super.onStart()
        //TODO start service here
        //startForegroundService()
        //TODO bind to service here
    }

    //TODO unbind from service
    override fun onStop() {
        super.onStop()
    }


    //TODO uncomment after done testing
//    override fun onDestroy() {
//        super.onDestroy()
//        unSubscribe?.remove()
//    }


//    inner class DatabaseListenerClass: AppCompatActivity(),NewDataInterface{
//        override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//            val bundle= Bundle().apply {
//                putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//            }
//            val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//                putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//            }
//            startActivity(intent)
//        }
//
//    }
}

class DatabaseListenerClass(val mainContext: Context): AppCompatActivity(), NewDataInterface{

//    private lateinit var mainContext: Context
//    var mainContext: Context?=null
//
//    constructor(){
//
//    }
//
//    constructor(mContext:Context) : this(){
//        mainContext=mContext
//    }

//    val mainContext: Context?=mainActivityContext ?: null
//    var mContext: Context?=null
//    init{
//        if(mainActivityContext!=null){
//
//           mainContext=mainActivityContext
//
//        }
//    }
    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
        val bundle= Bundle().apply {
            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
//            putParcelable(SERMON_KEY, sermon)
        }

        Timber.i("mainContext= $mainContext")
        val intent = Intent(mainContext, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
        }

        @MainThread
        startActivity(intent)
    }

}

