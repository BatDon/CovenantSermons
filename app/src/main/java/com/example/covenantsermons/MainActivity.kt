package com.example.covenantsermons

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.covenantsermons.modelDatabase.Sermon
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.viewmodel.MainViewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(),NewDataInterface{
//class MainActivity : AppCompatActivity(),ViewInterface, ViewInterface.NewDataInterface{
    private lateinit var mainViewModel: MainViewModel

    private var sermonArrayList:ArrayList<Sermon> =ArrayList<Sermon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

//        MainActivityAccesser(this)

        //setUpViewModel()

        //TODO uncomment after done testing used to fill recyclerview
//        var sermonArrayList=ArrayList<Sermon>()
        //DatabaseListenerClass(this@MainActivity)
        getPodcastsFromDatabase(this@MainActivity)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_media -> {
                playPodcastData()
                return true
            }
            R.id.menu_donations -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

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

    fun playPodcastData(){
                Timber.i("playPodcastData function called")
        val bundle= Bundle().apply {
            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
//            putParcelable(SERMON_KEY, sermon)
        }

//        Timber.i("mainContext= $mainContext")
        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
        }

//        @MainThread
        startActivity(intent)
    }


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

    fun createIntent(bundle:Bundle){
        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
        }
        startActivity(intent)
    }

//    fun playPodcastActivityNewData(sermonArrayList: ArrayList<Sermon>) {
//        Timber.i("playPodcastActivityNewData function called")
//        val bundle= Bundle().apply {
//            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//        }
//
////        Timber.i("mainContext= $mainContext")
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//        }
//
////        @MainThread
//        startActivity(intent)
//    }
//    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//        Timber.i("playPodcastActivityNewData function called")
//        val bundle= Bundle().apply {
//            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
//    //            putParcelable(SERMON_KEY, sermon)
//        }
//
//    //        Timber.i("mainContext= $mainContext")
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//        }
//
//    //        @MainThread
//        startActivity(intent)
//    }

    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
        Timber.i("playPodcastActivityNewData function called")
        this.sermonArrayList=sermonArrayList

    }

//    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//        Timber.i("playPodcastActivityNewData function called")
//        val bundle= Bundle().apply {
//            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//        }
//
////        Timber.i("mainContext= $mainContext")
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//        }
//
////        @MainThread
//        startActivity(intent)
//    }
}

//class DatabaseListenerClass(val mainContext: Context): AppCompatActivity(), NewDataInterface{

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
//    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//        val bundle= Bundle().apply {
//            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//        }
//
//        Timber.i("mainContext= $mainContext")
//        val intent = Intent(mainContext, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//        }
//
//        @MainThread
//        startActivity(intent)
//    }

//    fun createIntent(){
//        val intent = Intent(mainContext, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//        }
//    }

//}

