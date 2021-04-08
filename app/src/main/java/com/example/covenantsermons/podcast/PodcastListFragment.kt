package com.example.covenantsermons.podcast

import android.content.Context
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.MasterFragmentViewModel
import com.example.covenantsermons.R
import com.example.covenantsermons.extensions.combineSermonLists
import com.example.covenantsermons.extensions.sermonInDownloadedList
import com.example.covenantsermons.extensions.sermonRemoveFromDownloadedList
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.modelClass.SermonEntity
import com.example.covenantsermons.modelClass.SermonEntity.Companion.fromSermonToSermonEntity
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.player.PlayerService
import com.example.covenantsermons.player.PlayerService.Companion.NOW_PLAYING_CHANNEL_ID
import com.example.covenantsermons.player.PlayerService.Companion.NOW_PLAYING_NOTIFICATION_ID
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import com.example.covenantsermons.viewmodel.DownloadViewModel
import com.example.covenantsermons.viewmodel.SermonViewModel
import kotlinx.android.synthetic.main.podcast_item.view.*
import kotlinx.android.synthetic.main.podcast_list_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


//class PodcastListFragment : Fragment(R.layout.podcast_list_fragment) {
class PodcastListFragment : Fragment() {

    //val podcastListViewModel: PodcastListViewModel by viewModel()
//    R.layout.podcast_list_fragment

    private val podcastListViewModel: PodcastListViewModel by viewModel()
    private val playerViewModel: PlayerViewModel by sharedViewModel()
    private val masterFragmentViewModel: MasterFragmentViewModel by sharedViewModel()
    private val downloadViewModel: DownloadViewModel by viewModel()
    private val sermonViewModel: SermonViewModel by viewModel()
    private val playerService: PlayerService by inject()
//    private var downloadViewModel: DownloadViewModel? =null


    //    private val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//    private lateinit var podcastListViewModel: PodcastListViewModel
    private var sermonPlayArrayList: ArrayList<Sermon?> = ArrayList<Sermon?>()
    private var sermonArrayList: ArrayList<Sermon?> = ArrayList<Sermon?>()
    private var sermonEntityArrayList: ArrayList<SermonEntity> = ArrayList<SermonEntity>()
    private var combinedSermonArrayList: ArrayList<Sermon?> = ArrayList<Sermon?>()
    private var podcastsDownloaded: ArrayList<Sermon?> = ArrayList<Sermon?>()
    private lateinit var podcastAdapter: PodcastAdapter
    private var currentPlaying = Sermon()
    private var callSetPodcastViewModel:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Timber.i("onCreateView called in PodcastListFragment")
        Timber.i("onCreateView callSetPodcastViewModel= $callSetPodcastViewModel")
        // podcastListViewModel=ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
        return inflater.inflate(R.layout.podcast_list_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val playerView=requireActivity().findViewById<View>(R.id.player_view)
//    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        val mainActivity: MainActivity? = activity as MainActivity
        mainActivity?.hideUpButton()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // podcastListViewModel.getPodcastsFromDatabase()


        setAdapter()
        setRVLayoutManager()
        setPodcastViewModelForPodcasts()
        setPodcastViewModelForPodcastsDownloaded()
        setSermonViewModel()
        setPlayerViewModel()
        //adapterOnClickListener()

    }

    private fun setAdapter() {
        Timber.i("setAdapter called")

        podcastAdapter = PodcastAdapter(activity, sermonArrayList).also { it ->
            it.onItemClick = { sermon ->
                //Toast.makeText(activity, "title ${sermon.title} audio file${sermon.audioFile}", Toast.LENGTH_LONG).show()
                Timber.i("on Click called title ${sermon.title}")
                Timber.i("sermon= $sermon")
                Timber.i("podcastListViewModel.getDownloadedPodcasts()= ${podcastListViewModel.getDownloadedPodcasts()}")
                if (podcastsDownloaded.sermonInDownloadedList(sermon)) {
                    showPodcastDetailFragment(sermon)
                } else {
                    Toast.makeText(activity, "Please download first.", Toast.LENGTH_LONG).show()
                }
            }

            it.onDownloadCancelPlayClick = { sermon ->
                Timber.i("onDownloadCancelPlayClick called ${sermon.title}")
                Timber.i("onDownloadCancelPlayClick sermon= $sermon")


                if (podcastsDownloaded.sermonInDownloadedList(sermon)) {
                    showPodcastDetailFragment(sermon)
                } else {

                    if(!checkIfNetworkConnection()){
                        Toast.makeText(activity, "Internet connection necessary for downloading", Toast.LENGTH_SHORT).show()
                    }

                    sermon.date?.let { date ->
                        if (sermonViewModel.count(date) == 0) {
                            downloadViewModel.let {
                                downloadViewModel.sermonArrayList.clear()
                                downloadViewModel.sermonArrayList.add(sermon)
                                it.startWork(sermon)
                            }
                        }
                        //TODO remove only for testing
                        else {
                            Timber.i("SermonEntity already exists")
                        }
                        //}
                        //downloadViewModel.startWork(sermon)
//                //TODO pass to sermon or service so user can close app and still download
//                //val httpsReference = storage.getReferenceFromUrl(sermon.audioFile)
                    }


//                podcast_list_rv.adapter = it
                }
                //podcast_list_rv.adapter = it
            }
            podcast_list_rv.adapter = it

//        podcast_list_rv.adapter = podcastAdapter
        }
    }

    private fun checkIfNetworkConnection():Boolean{
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    private fun showPodcastDetailFragment(sermon: Sermon) {
        Timber.i("podcastListViewModel.podcastsDownloaded.value= ${podcastListViewModel.podcastsDownloaded.value}")

        podcastListViewModel.podcastsDownloaded.value?.let { sermonArrayList -> playerViewModel.play(sermon, sermonArrayList) }
        findNavController().navigate(
                R.id.action_mainFragment_to_podcastDetailsFragment,
                bundleOf(
                        PodcastDetailsFragment.podcastSermonArgument to sermon,
                ), null, null)
    }

    private fun setRVLayoutManager() {
        Timber.i("setRVLayoutManager called")
        val mLayoutManager = LinearLayoutManager(podcast_list_rv.context)
        podcast_list_rv.layoutManager = mLayoutManager

        val mDividerItemDecoration = DividerItemDecoration(podcast_list_rv.context,
                mLayoutManager.orientation)
        podcast_list_rv.addItemDecoration(mDividerItemDecoration)

        podcast_list_rv.setHasFixedSize(true)
        swipePodcastListener(podcast_list_rv)
        if (sermonArrayList.size > 0) {
            podcast_list_rv.visibility = View.VISIBLE
            progress_bar.visibility = View.INVISIBLE
        }
    }

    private fun setPodcastViewModelForPodcasts() {
        Timber.i("setPodcastViewModel called")
        activity?.let { getPodcastsFromDatabase(podcastListViewModel, it) }
        podcastListViewModel.podcasts.observe(viewLifecycleOwner, Observer { list ->
            Timber.i("podcasts observer called")
            sermonArrayList = ArrayList(list)
            Timber.i("podcastListViewModel.podcasts sermonArrayList= $sermonArrayList")
            if (sermonEntityArrayList.size > 0) {
                createCombinedSermonArrayList()
                callSetPodcastViewModel = false
            } else {
                combinedSermonArrayList = sermonArrayList
            }
            sermonListUpdated()
        })
    }

    private fun setPodcastViewModelForPodcastsDownloaded() {
        podcastListViewModel.podcastsDownloaded.observe(viewLifecycleOwner, Observer { list ->
            Timber.i("podcastsDownloaded observer called")
            podcastsDownloaded = ArrayList(list)
            Timber.i("podcastsDownloaded= $podcastsDownloaded")
            if (sermonEntityArrayList.size > 0) {
                createCombinedSermonArrayList()
            } else {
                combinedSermonArrayList = sermonArrayList
            }
            sermonListUpdated()
        })

        masterFragmentViewModel.toShowAppBar(true)
    }

    private fun setSermonViewModel() {
        sermonViewModel.allSermons.observe(viewLifecycleOwner, Observer { list ->
            sermonEntityArrayList = ArrayList(list)
            Timber.i("sermonEntityArrayList= $sermonEntityArrayList")
            Timber.i("sermonEntityArrayList size= ${sermonEntityArrayList.size}")
            if (callSetPodcastViewModel) {
                Timber.i("setSermonViewModel callSetPodcastViewModel=true")
                setPodcastViewModelForPodcasts()
            }
            if (sermonEntityArrayList.size > 0) {
                createCombinedSermonArrayList()

            } else {
                combinedSermonArrayList = sermonArrayList
            }

        })
    }

    private fun setPlayerViewModel() {
        playerViewModel.currentlyPlaying.observe(viewLifecycleOwner, Observer { currentPlayingSermon ->
            currentPlaying = currentPlayingSermon
        })
    }


    private fun createCombinedSermonArrayList() {
        combinedSermonArrayList = sermonEntityArrayList.combineSermonLists(sermonArrayList)
        sermonListUpdated()
    }

    private fun sermonListUpdated() {
        Timber.i("sermonListUpdated called")
        Timber.i("combinedSermonArrayList= $combinedSermonArrayList")
        podcastAdapter.updateSermonList(combinedSermonArrayList)
        podcastAdapter.notifyDataSetChanged()
        podcast_list_rv.visibility = View.VISIBLE
        progress_bar.visibility = View.INVISIBLE
    }

    private fun swipePodcastListener(podcastRecyclerView: RecyclerView) {
        val podcastItemSwipeCallback = object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (viewHolder != null) {
                    val foregroundView: View = (viewHolder as PodcastAdapter.ItemViewHolder).viewBinding.root.view_foreground
                    getDefaultUIUtil().onSelected(foregroundView)
                }
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) {

//                val sermonRowIndex=viewHolder.adapterPosition
//                podcastAdapter.removeItem(sermonRowIndex)

                val sermonViewHolder = viewHolder as PodcastAdapter.ItemViewHolder
                val sermon = podcastAdapter.getSermonAt(sermonViewHolder.adapterPosition)


                Toast.makeText(activity, "sermon swiped= $sermon", Toast.LENGTH_SHORT).show()
                Timber.i("sermon swiped= $sermon")

                if (sermon != null) {

                    val sermonArrayList = sermonListFromSermonEntityList()
                    val sermonInDownloadList = sermonArrayList.sermonInDownloadedList(sermon)
                    Timber.i("sermonInDownloadList= $sermonInDownloadList")
                    if (sermonInDownloadList) {
                        Timber.i("sermonInDownloadedList if statement")
                        val sermonEntity = SermonEntity.fromSermonToSermonEntity(sermon)
                        Timber.i("onSwiped before delete sermonEntityList= $sermonEntityArrayList")
                        val sermonEntityList = sermonEntityArrayList.toList<SermonEntity>()
                        sermonViewModel.delete(sermonEntity)
                        callSetPodcastViewModel = true


                        Timber.i("onSwiped after delete sermonEntityList= $sermonEntityList")
                        val sermonList = SermonEntity.fromSermonEntityToSermon(sermonEntityList)
                        val sermonDownloadedList = ArrayList(sermonList).sermonRemoveFromDownloadedList(sermon)
                        chooseList(sermonDownloadedList)
                        Timber.i("sermonDownloadedList= $sermonDownloadedList")
                        podcastListViewModel.setDownloadedPodcasts(sermonDownloadedList)
                        //activity?.let { getPodcastsFromDatabase(podcastListViewModel, it) }
//                        setPodcastViewModel()
//                        setSermonViewModel()
                        if (currentPlaying.date != null && sermon?.date != null) {
                            Timber.i("currentPlaying= $currentPlaying")
                            if (currentPlaying.date?.compareTo(sermon.date!!, false) == 0) {
                                playerViewModel.emptyPlayList()
                                val notificationManager = activity?.applicationContext?.let { NotificationManagerCompat.from(it) }
                                Timber.i("onSwiped notificationManager= $notificationManager")
                                notificationManager?.cancel(NOW_PLAYING_CHANNEL_ID, NOW_PLAYING_NOTIFICATION_ID)

                            }
                        }

                        if (sermonDownloadedList.size > 0) {
                            Timber.i("if sermonDownloadedList.size= ${sermonDownloadedList.size}")
                            playerViewModel.createPlaylist(null, sermonDownloadedList.toList())
                        } else {
                            Timber.i("else sermonDownloadedList.size= ${sermonDownloadedList.size}")
                        }
//                        setPodcastViewModel()
//                        setSermonViewModel()
                    } else {
                        Toast.makeText(activity, "Can't remove undownloaded sermon= $sermon", Toast.LENGTH_SHORT).show()
                        Timber.i("Can't remove undownloaded sermon= $sermon")
                        sermonListUpdated()
                    }
                }
            }

            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val foregroundView: View = (viewHolder as PodcastAdapter.ItemViewHolder).viewBinding.root.view_foreground
                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                val foregroundView: View = (viewHolder as PodcastAdapter.ItemViewHolder).viewBinding.root.view_foreground
                getDefaultUIUtil().clearView(foregroundView)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val foregroundView: View = (viewHolder as PodcastAdapter.ItemViewHolder).viewBinding.root.view_foreground
                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive)
            }


        }
        val podcastItemTouchHelper = ItemTouchHelper(podcastItemSwipeCallback)
        podcastItemTouchHelper.attachToRecyclerView(podcastRecyclerView)
    }


    fun sermonListFromSermonEntityList(): ArrayList<Sermon?> {
        val sermonEntityList = sermonEntityArrayList.toList<SermonEntity>()
        Timber.i("onSwiped after delete sermonEntityList= $sermonEntityList")
        val sermonList = SermonEntity.fromSermonEntityToSermon(sermonEntityList)
        return ArrayList(sermonList)
    }

    fun PodcastAdapter.ItemViewHolder.listen(event: (position: Int, type: Int) -> Unit): PodcastAdapter.ItemViewHolder {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }

    fun chooseList(sermonDownloadedArrayList: ArrayList<Sermon>){
        Timber.i("chooseList called sermonDownloadedArrayList= $sermonDownloadedArrayList")
        combinedSermonArrayList = if (sermonDownloadedArrayList.size > 0) {
            Timber.i("chooseList if statement called")
            //combinedSermonArrayList = combinedSermonArrayList = sermonEntityArrayList.combineSermonLists(sermonArrayList)
            val sermonDownloadedEntityList=fromSermonToSermonEntity(sermonDownloadedArrayList.toList())
            ArrayList(sermonDownloadedEntityList).combineSermonLists(sermonArrayList)
            //sermonListUpdated()
        } else {
            sermonArrayList
        }
        sermonListUpdated()
    }


    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
}


