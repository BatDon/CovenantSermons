package com.example.covenantsermons

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.covenantsermons.databinding.ActivityMainBinding
import com.example.covenantsermons.modelDatabase.Sermon
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import com.example.covenantsermons.podcast.PodcastAdapter
import com.google.android.exoplayer2.ExoPlayer
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(){
    private val playerViewModel: PlayerViewModel by viewModel()
    private val exoPlayer: ExoPlayer by inject()
//class MainActivity : AppCompatActivity(),ViewInterface, ViewInterface.NewDataInterface{
//    private lateinit var mainViewModel: MainViewModel


    private val podcastListViewModel: PodcastListViewModel by viewModel()

//    private var sermonArrayList:ArrayList<Sermon> =ArrayList<Sermon>()
    private lateinit var sermonArrayList:ArrayList<Sermon?>
//    private lateinit var itemsCells: ArrayList<String?>
    private lateinit var podcastAdapter: PodcastAdapter

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        Timber.plant(Timber.DebugTree())
        //SermonDatabase().getPodcastsFromDatabase()
        getPodcastsFromDatabase(podcastListViewModel)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        //activityMainBinding.toolbar.setupWithNavController(navController, appBarConfiguration)
        activityMainBinding.playerView.player=exoPlayer
//        activityMainBinding.main_activity_player_group.player_view.player = exoPlayer



//        if(!this::sermonArrayList.isInitialized){
//            podcastListViewModel.getPodcastsFromDatabase()
//        }else{
//
//        }?:SermonDatabase().getPodcastsFromDatabase(this@MainActivity))

//        MainActivityAccesser(this)

//        setUpViewModel()
//
//        setAdapter()
//
//        setRVLayoutManager()



        //TODO uncomment after done testing used to fill recyclerview
//        var sermonArrayList=ArrayList<Sermon>()
        //DatabaseListenerClass(this@MainActivity)
//        SermonDatabase().getPodcastsFromDatabase(this@MainActivity)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_media -> {
                //playPodcastData()
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

//    fun playPodcastData(){
//                Timber.i("playPodcastData function called")
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

//    fun createIntent(bundle:Bundle){
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY,bundle)
//        }
//        startActivity(intent)
//    }

//    fun setUpViewModel(){
//        podcastListViewModel.podcasts.observe(this, Observer { list ->
//            sermonArrayList=ArrayList(list)
//        })
//    }



//    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//        Timber.i("playPodcastActivityNewData function called")
//        this.sermonArrayList=sermonArrayList
//
//    }
}



