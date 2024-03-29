package com.batdon.covenantsermons


import android.app.ActivityManager
import android.app.SearchManager
import android.content.Intent
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
import com.batdon.covenantsermons.databinding.ActivityMainBinding
import com.batdon.covenantsermons.modelClass.Sermon
import com.batdon.covenantsermons.modelClass.SermonEntity
import com.batdon.covenantsermons.modelClass.SermonEntity.Companion.DOWNLOADING_STATE_IMAGES_PLAY
import com.batdon.covenantsermons.modelClass.SermonEntity.Companion.fromSermonEntityToSermon
import com.batdon.covenantsermons.modelDatabase.authenticateAnonymousUser
import com.batdon.covenantsermons.player.PlayerViewModel
import com.batdon.covenantsermons.player.PodcastListViewModel
import com.batdon.covenantsermons.podcast.PodcastAdapter
import com.batdon.covenantsermons.viewmodel.DownloadViewModel
import com.batdon.covenantsermons.viewmodel.SermonViewModel
import com.batdon.covenantsermons.workers.AudioWorker
import com.batdon.covenantsermons.workers.ImageWorker
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
    private val sermonViewModel: SermonViewModel by viewModel()
//    private val podcastListViewModel: PodcastListViewModel by viewModel()
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

    private var toolbar: Toolbar? =null
    private var collapsingToolbar: CollapsingToolbarLayout? =null

    private lateinit var sermonEntityList: ArrayList<SermonEntity>

    var imagePath:Boolean=false
    var audioPath:Boolean=false

    private var currentSermonDownloading : Sermon?=null

    var mIntent: Intent?=null

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //val storageRoot=this.filesDir.toString() + "/"
        //playerViewModel.setStorageRoot(storageRoot)
       // Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")

        Timber.i("onCreate savedInstanceState= $savedInstanceState")

        intent.let {
            if (intent == null) {
                Timber.i("intent equals null")
            } else {
                mIntent = intent

                if (intent.hasCategory("PlayerService")) {
                    Timber.i("intent hasCategory PlayerService")
                    intent = mIntent
                    Timber.i("intent= $intent")

//                    Timber.i("intent hasCategory PlayerService 2")
//                    Timber.i("intent hasCategory PlayerService 3")
//                    val tStart = System.currentTimeMillis()
//                    val tEnd = System.currentTimeMillis()
//                    val timeTaken=tEnd-tStart
//                    if(timeTaken > )
//                    return
                }
                Timber.i("intent= $intent")
                Timber.i("intent flags= ${intent.flags}")
            }
        }

//        val tStart = System.currentTimeMillis()
//        val tEnd = System.currentTimeMillis()



        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        //SermonDatabase().getPodcastsFromDatabase()
        authenticateAnonymousUser(podcastListViewModel, this)


        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        //up button for toolbar
//        toolbar = activityMainBinding.appBarCollapsingToolbar.toolbar
//        setSupportActionBar(toolbar)
//        toolbar = activityMainBinding.appBarCollapsingToolbar.toolbar
        //TODO make sure appBarToolbar exists. It doesn't in landscape mode
        toolbar = activityMainBinding.appBarToolbar
        if(toolbar!=null){
            setSupportActionBar(toolbar)
        }


        collapsingToolbar= activityMainBinding.collapsingToolbar

        collapsingToolbar?.title = getString(R.string.app_name_with_space)
        collapsingToolbar?.setExpandedTitleTextAppearance(R.style.AppBarExpanded)
        collapsingToolbar?.setCollapsedTitleTextAppearance(R.style.AppBarCollapsed)


        //activityMainBinding.toolbar.setupWithNavController(navController, appBarConfiguration)
        activityMainBinding.playerView.player = exoPlayer
        activityMainBinding.playerView.requestFocus()

//        error here being called twice from service. PlayerService doesn't have the error'

                mIntent=null
                val mngr = getSystemService(ACTIVITY_SERVICE) as ActivityManager

        val taskList = mngr.getRunningTasks(10)

        if (taskList[0].numActivities == 1 && taskList[0].topActivity!!.className == this.javaClass.name) {
            Timber.i("This is last activity in the stack")
//            Timber.i("Destroy this activity")
//            onDestroy()
        }
        Timber.i("taskList= $taskList")

//        var i=0
//        intent.let {
//            if(intent==null){
//                Timber.i("intent equals null")
//            }
//            else{
//                mIntent=intent
//                if(intent.hasCategory("PlayerService")){
//                    Timber.i("intent hasCategory PlayerService")
//                }
//                Timber.i("intent= $intent")
//                Timber.i("intent flags= ${intent.flags}")
//            }
//            if(i==0) {
//                Timber.i("i= $i")
//                i++
//                Timber.i("i= $i")
//                val sermon = exoPlayer.currentTag as? Sermon
//                Timber.i("exoplayer.currentTag sermon $sermon")
//
//                Timber.i("currentWindowIndex sermon= $sermon")
//                if (sermon != null) {
//                    setCurrentSermonTitle(sermon)
//                }
//            }
//            val aTestInt=1
//        }

        if (taskList[1].numActivities == 1 && taskList[1].topActivity!!.className == this.javaClass.name) {
            Timber.i("Destroy this activity")
            onDestroy()
        }


        setUpViewModels()



        masterFragmentViewModel.toShowAppBar(true)

        masterFragmentViewModel.showAppBar.observe(this, Observer { showAppBar ->

            activityMainBinding.appBar?.setExpanded(showAppBar, showAppBar)

            ViewCompat.setNestedScrollingEnabled(activityMainBinding.nestedScrollView, showAppBar)

            val appBarParams = activityMainBinding.appBar?.layoutParams as CoordinatorLayout.LayoutParams
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.i("onNewIntent called intent= $intent")
    }

    //    override fun onResume() {
//        super.onResume()
//        intent.let {
//            val sermon = exoPlayer.currentTag as? Sermon
//            Timber.i("exoplayer.currentTag sermon $sermon")
//
//            Timber.i("currentWindowIndex sermon= $sermon")
//            if (sermon != null) {
//                setCurrentSermonTitle(sermon)
//            }
//        }
//
//    }

    private fun setUpViewModels(){
        playerViewModel.currentlyPlaying.observe(this, Observer { sermon: Sermon ->
            setCurrentSermonTitle(sermon)

//            activityMainBinding.currentSermonTitle.text = sermon.title
//            Timber.i("sermon Title changed")
//            Timber.i("sermon.title is ${sermon.title}")
        })

        sermonViewModel.allSermons.observe(this, Observer { sermonEntityListLiveData: List<SermonEntity> ->
            Timber.i("list SermonEntity observer called $sermonEntityListLiveData")
            this@MainActivity.sermonEntityList = ArrayList<SermonEntity>(sermonEntityListLiveData)
            Timber.i("list SermonEntity observer called sermonEntityList $sermonEntityList")

            setDownloadedSermons()

//            activityMainBinding.currentSermonTitle.text = sermon.title
//            Timber.i("sermon Title changed")
//            Timber.i("sermon.title is ${sermon.title}")
        })
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
                        imagePath = true
                        downloadViewModel.imageFileLocation = workInfo.outputData.getString(ImageWorker.KEY_IMAGE_BITMAP_FILE_PATH)
//                        if(imagePath && audioPath){
//                            //val sermon=downloadViewModel.currentSermon
//                            createSermonForRoom()
//                            imagePath=false
//                            audioPath=false
//                        }
                    }
                    AUDIO_FILE -> {
                        audioPath = true
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
        if(downloadViewModel.sermonArrayList.isNotEmpty()) {
            val sermon = downloadViewModel.sermonArrayList[0]
            val imageFileLocation = downloadViewModel.imageFileLocation
            val audioFileLocation = downloadViewModel.audioFileLocation
            Timber.i("downloadViewModel.imageFileLocation $imageFileLocation audioFileLocation= $audioFileLocation")

            Timber.i("sermon = $sermon")
            val sermonEntity = SermonEntity(sermon?.date!!, sermon.title, sermon.pastorName, audioFileLocation, sermon.duration, imageFileLocation, DOWNLOADING_STATE_IMAGES_PLAY)
            Timber.i("sermonEntity= $sermonEntity")
            sermonViewModel.insert(sermonEntity)
        }

        //setDownloadedSermons()

        //TODO Remove only for testing
//        val sermonEntityArrayList=ArrayList<SermonEntity>()
//        sermonEntityArrayList.add(sermonEntity)
//        sermonEntityArrayList.combineSermonLists(podcastListViewModel.)
//        sermonViewModel.allSermons.combineSermonLists()


    }

    private fun setDownloadedSermons(){
        val downloadedSermonList=roomSermonsToDownloadedSermonsList()
        podcastListViewModel.setDownloadedPodcasts(downloadedSermonList)
        Timber.i("downloadedSermonList= $downloadedSermonList")
    }

//    TODO need to finish this method
    private fun roomSermonsToDownloadedSermonsList():ArrayList<Sermon>{

        var sermonDownloadedArrayList:ArrayList<Sermon>?=null
    Timber.i("sermonViewModel.allSermons.value ${sermonViewModel.allSermons.value}")

//        sermonViewModel.allSermons.value?.let{ sermonEntityList ->
        sermonEntityList.let{ sermonEntityList ->
            Timber.i("sermonEntityList= $sermonEntityList")
            sermonDownloadedArrayList = if(sermonEntityList.isNotEmpty()){
                ArrayList<Sermon>(fromSermonEntityToSermon(sermonEntityList))
                //return@roomSermonsToDownloadedSermonsList ArrayList<Sermon>(fromSermonEntityToSermon(sermonEntityList))
            }else{
                Timber.i("else called ")
                ArrayList<Sermon>()
                //return@roomSermonsToDownloadedSermonsList ArrayList<Sermon>()
            }

        }

        Timber.i("sermonDownloadedArrayList $sermonDownloadedArrayList")
        return sermonDownloadedArrayList!!

       // podcastListViewModel.setDownloadedPodcasts(ArrayList(sermonViewModel.allSermons.value?.let { SermonEntity.fromSermonEntityToSermon(it) }))
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
            R.id.action_church_facebook -> {
                searchWeb(getString(R.string.facebook_url))
//                https://www.facebook.com/covenantpresgj
                return true
            }
            R.id.action_church_website -> {
                searchWeb(getString(R.string.website_url))
//                https://www.covpresgj.org/
                return true
            }
            R.id.action_church_donation -> {
                searchWeb(getString(R.string.donation_url))
//                https://www.facebook.com/covenantpresgj
                //playPodcastData()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun searchWeb(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("destroying MainActivity")
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



