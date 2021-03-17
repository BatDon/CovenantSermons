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
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.covenantsermons.databinding.ActivityMainBinding
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.modelClass.SermonEntity
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import com.example.covenantsermons.podcast.PodcastAdapter
import com.example.covenantsermons.viewmodel.DownloadViewModel
import com.example.covenantsermons.viewmodel.SermonViewModel
import com.example.covenantsermons.workers.AudioWorker
import com.example.covenantsermons.workers.ImageWorker
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.exo_playback_control_view.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity(){
    private val playerViewModel: PlayerViewModel by viewModel()
    private val exoPlayer: ExoPlayer by inject()
    private val workManager: WorkManager by inject()
//    private val downloadViewModelFactory: DownloadViewModelFactory by inject()
//    private val downloadViewModel: DownloadViewModel by viewModel(){
//        downloadViewModelFactory
//    }
//    private val downloadViewModel: DownloadViewModel by viewModel()

//class MainActivity : AppCompatActivity(),ViewInterface, ViewInterface.NewDataInterface{
//    private lateinit var mainViewModel: MainViewModel


    private val sermonViewModel: SermonViewModel by viewModel()
    private val podcastListViewModel: PodcastListViewModel by viewModel()
    private val masterFragmentViewModel: MasterFragmentViewModel by viewModel()
    private val downloadViewModel: DownloadViewModel by viewModel()
//    private var sermonArrayList:ArrayList<Sermon> =ArrayList<Sermon>()
    private lateinit var sermonArrayList: ArrayList<Sermon?>
//    private lateinit var itemsCells: ArrayList<String?>
    private lateinit var podcastAdapter: PodcastAdapter

    lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbar: CollapsingToolbarLayout

    var imagePath:Boolean=false
    var audioPath:Boolean=false

    private var currentSermonDownloading : Sermon?=null

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
        getPodcastsFromDatabase(podcastListViewModel, this)


        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        //up button for toolbar
//        toolbar = activityMainBinding.appBarCollapsingToolbar.toolbar
//        setSupportActionBar(toolbar)
//        toolbar = activityMainBinding.appBarCollapsingToolbar.toolbar
        toolbar = activityMainBinding.appBarToolbar
        setSupportActionBar(toolbar)

        collapsingToolbar= activityMainBinding.collapsingToolbar

        collapsingToolbar.title = getString(R.string.app_name_with_space);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.AppBarExpanded);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.AppBarCollapsed);


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

        playerViewModel.currentlyPlaying.observe(this, Observer { sermon:Sermon ->
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

//        downloadViewModel.outputWorkInfos.observe(this, workInfosObserver())
     //   currentSermonDownloadingListener()
        downloadViewModel.outputImageWorkInfos.observe(this, workInfosObserver(IMAGE_FILE))
        downloadViewModel.outputAudioWorkInfos.observe(this, workInfosObserver(AUDIO_FILE))
    }

//    private fun currentSermonDownloadingListener(){
//        Timber.i("currentSermonDownloadingListener called")
//        Timber.i("before listener triggered currentSermonDownloading= $currentSermonDownloading")
//
//        downloadViewModel.currentSermonDownloading.observe(this, Observer { sermonArrayList ->
//            Timber.i("currentSermonDownloadingListener triggered")
//            currentSermonDownloading=ArrayList(sermonArrayList)[0]
//            Timber.i("currentSermonDownloadingListener triggered sermon= $currentSermonDownloading")
//
//        })
//    }



//    fun observeImageAudioDownload() {
//        downloadViewModel.outputImageAudioWorkInfos.observe(this, workInfosObserver())
//    }

    private fun workInfosObserver(imageOrAudio: String): Observer<List<WorkInfo>> {
        Timber.i("workInfosObserver called")

        return Observer { listOfAllWorkInfos ->

            if (listOfAllWorkInfos.isNullOrEmpty()) {
                Timber.i("listOfAllWorkInfos is null or empty")
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            Timber.i("listOfAllWorkInfos size= ${listOfAllWorkInfos.size}")
            val workInfo = listOfAllWorkInfos[0]

            //bosh image and audio finished
            if (workInfo.state.isFinished) {
                Timber.i("imagePath= $imagePath audioPath= $audioPath")
                Timber.i("work finished")
                Timber.i("workInfo.outputData ${workInfo.outputData}")
                when (imageOrAudio) {
                    IMAGE_FILE -> {
                        imagePath=true
                        downloadViewModel.imageFileLocation = workInfo.outputData.getString(ImageWorker.KEY_IMAGE_BITMAP_FILE_PATH)
//                        if(imagePath && audioPath){
//                            //val sermon=downloadViewModel.currentSermon
//                            createSermonForRoom()
//                            imagePath=false
//                            audioPath=false
//                        }
                    }
                    AUDIO_FILE -> {
                        audioPath=true
                        downloadViewModel.audioFileLocation = workInfo.outputData.getString(AudioWorker.KEY_AUDIO_FILE_PATH)
                        //downloadViewModel.currentSermonDownloading = workInfo.outputData.
//                        downloadViewModel.currentSermonDownloading = workInfo.outputData.getParcelable(AudioWorker.KEY_SERMON_DOWNLOADING)
//                        if(imagePath && audioPath) {
//                            createSermonForRoom()
//                            imagePath=false
//                            audioPath=false
//                        }
                    }
                }
                if(imagePath && audioPath){
                    //val sermon=downloadViewModel.currentSermon
                    createSermonForRoom()
                    imagePath=false
                    audioPath=false
                }
            } else {
                Timber.i("work is still in progress")
                when (imageOrAudio) {
                    IMAGE_FILE -> downloadViewModel.imageFileLocation = null
                    AUDIO_FILE -> downloadViewModel.audioFileLocation = null
                }
            }
        }
    }

    @Synchronized
    fun createSermonForRoom(){
        Timber.i("createSermonForRoom called")
        //val sermon=downloadViewModel.currentSermonDownloading.value
//        val sermon=currentSermonDownloading
        val sermon=downloadViewModel.sermonArrayList[0]
        val imageFileLocation=downloadViewModel.imageFileLocation
        val audioFileLocation=downloadViewModel.audioFileLocation
        Timber.i("downloadViewModel.imageFileLocation $imageFileLocation audioFileLocation= $audioFileLocation")
        //TODO sermon equals null this is why there is an error
        Timber.i("sermon = $sermon")
        val sermonEntity = SermonEntity(sermon?.date!!, sermon.title, sermon.pastorName, audioFileLocation, sermon.duration, imageFileLocation)
        Timber.i("sermonEntity= $sermonEntity")
        sermonViewModel.insert(sermonEntity)


    }


//    private fun workInfosObserver(): Observer<List<WorkInfo>> {
//        return Observer { listOfWorkInfo ->
//
//            // Note that these next few lines grab a single WorkInfo if it exists
//            // This code could be in a Transformation in the ViewModel; they are included here
//            // so that the entire process of displaying a WorkInfo is in one location.
//
//            // If there are no matching work info, do nothing
//            if (listOfWorkInfo.isNullOrEmpty()) {
//                return@Observer
//            }
//
//            // We only care about the one output status.
//            // Every continuation has only one worker tagged TAG_OUTPUT
//            val imageWorkInfo = listOfWorkInfo[0]
//            val audioWorkInfo = listOfWorkInfo[1]
//
//            if (imageWorkInfo.state.isFinished && audioWorkInfo.state.isFinished) {
//                Timber.i("work finished")
//            } else {
//                Timber.i("work hasn't finished yet")
//            }
//        }
//    }






//        workManager.getWorkInfoByIdLiveData(ImageWorker.id)
//                .observe(this, Observer { info ->
//                    if (info != null && info.state.isFinished) {
//                        val myResult = info.outputData.(KEY_IMAGE_BITMAP,
//                                0)
//                        // ... do something with the result ...
//                    }
//                    if(info != null && info.state.isCancelled) {
//                        val myResult=null
//                    }
//                    if(info != null && info.state.isFailed) {
//                        val myResult=null
//                    }


    //}





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

    companion object{
        const val IMAGE_FILE="IMAGE_FILE"
        const val AUDIO_FILE="AUDIO_FILE"
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



