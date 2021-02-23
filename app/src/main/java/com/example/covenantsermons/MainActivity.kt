package com.example.covenantsermons


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
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
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.exo_playback_control_view.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity(){
    private val playerViewModel: PlayerViewModel by viewModel()
    private val exoPlayer: ExoPlayer by inject()

//class MainActivity : AppCompatActivity(),ViewInterface, ViewInterface.NewDataInterface{
//    private lateinit var mainViewModel: MainViewModel



    private val podcastListViewModel: PodcastListViewModel by viewModel()
    private val masterFragmentViewModel: MasterFragmentViewModel by viewModel()

//    private var sermonArrayList:ArrayList<Sermon> =ArrayList<Sermon>()
    private lateinit var sermonArrayList: ArrayList<Sermon?>
//    private lateinit var itemsCells: ArrayList<String?>
    private lateinit var podcastAdapter: PodcastAdapter

    lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var toolbar: Toolbar

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("onCreate called")



        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        Timber.plant(Timber.DebugTree())
        //SermonDatabase().getPodcastsFromDatabase()
        getPodcastsFromDatabase(podcastListViewModel)


        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        //up button for toolbar
//        toolbar = activityMainBinding.appBarCollapsingToolbar.toolbar
//        setSupportActionBar(toolbar)
//        toolbar = activityMainBinding.appBarCollapsingToolbar.toolbar
        toolbar = activityMainBinding.appBarToolbar
        setSupportActionBar(toolbar)


        //activityMainBinding.toolbar.setupWithNavController(navController, appBarConfiguration)
        activityMainBinding.playerView.player = exoPlayer
        activityMainBinding.playerView.requestFocus()

        intent.let {
            val sermon = exoPlayer.currentTag as? Sermon
            Timber.i("exoplayer.currentTag sermon $sermon")

            Timber.i("currentWindowIndex sermon= $sermon")
            if (sermon != null) {
                setCurrentSermonTitle(sermon)
            }
        }


//        activityMainBinding.main_activity_player_group.player_view.player = exoPlayer

        //TODO remove only for testing
//        Thread.sleep(2000)
//        playerViewModel.play(podcastListViewModel.transformLiveData()[0],podcastListViewModel.transformLiveData())


//        playerViewModel._currentlyPlaying.observe(this, Observer { sermon ->
//            Timber.i("sermon playing= $sermon")
//            Toast.makeText(applicationContext, "$sermon", Toast.LENGTH_SHORT).show()
//        })

        playerViewModel.currentlyPlaying.observe(this, Observer { sermon ->
            setCurrentSermonTitle(sermon)

//            activityMainBinding.currentSermonTitle.text = sermon.title
//            Timber.i("sermon Title changed")
//            Timber.i("sermon.title is ${sermon.title}")
        })


        masterFragmentViewModel.toShowAppBar(true)

        masterFragmentViewModel.showAppBar.observe(this, Observer { showAppBar ->

            activityMainBinding.appBar.setExpanded(showAppBar, showAppBar)

            ViewCompat.setNestedScrollingEnabled(activityMainBinding.nestedScrollView, showAppBar)

            val appBarParams = activityMainBinding.appBar.layoutParams as CoordinatorLayout.LayoutParams
            if (appBarParams.behavior == null)
                appBarParams.behavior = AppBarLayout.Behavior()
            val behaviour = appBarParams.behavior as AppBarLayout.Behavior
            behaviour.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return showAppBar
                }
            })
        })
    }





        //TODO uncomment after done testing used to fill recyclerview
//        var sermonArrayList=ArrayList<Sermon>()
        //DatabaseListenerClass(this@MainActivity)
//        SermonDatabase().getPodcastsFromDatabase(this@MainActivity)


  //  }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

            Timber.i("some item selected")
        when (item.itemId) {
            android.R.id.home -> {
                Timber.i("up button clicked")
//                val fragManager: FragmentManager = this.supportFragmentManager
//                if(fragManager.backStackEntryCount>0){
//                    fragManager.popBackStack()
//                }
                this.navController.popBackStack()
                return true
            }
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

    fun showUpButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    private fun setCurrentSermonTitle(sermon: Sermon){
        activityMainBinding.currentSermonTitle.text = sermon.title
        Timber.i("sermon Title changed")
        Timber.i("sermon.title is ${sermon.title}")
    }

    //TODO remove only for testing
    fun mediaButtonClicked(view: View){
        Timber.i("mediaButtonClicked")
        Timber.i("view.id= ${view.id}")
        Timber.i("activityMainBinding.playerView.exo_play.id= ${activityMainBinding.playerView.exo_play.id}")
        when (view.id) {
            activityMainBinding.playerView.exo_play.id -> {
                activityMainBinding.playerView.exo_play.visibility = INVISIBLE
                activityMainBinding.playerView.exo_pause.visibility = VISIBLE
                Timber.i("play clicked play invisible")
            }
            activityMainBinding.playerView.exo_pause.id -> {
                activityMainBinding.playerView.exo_pause.visibility = INVISIBLE
                activityMainBinding.playerView.exo_play.visibility = VISIBLE
                Timber.i("pause clicked pause invisible")
            }
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



