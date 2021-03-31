package com.example.covenantsermons.podcast

import android.graphics.Canvas
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
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.player.PlayerService
import com.example.covenantsermons.player.PlayerService.Companion.NOW_PLAYING_CHANNEL_ID
import com.example.covenantsermons.player.PlayerService.Companion.NOW_PLAYING_NOTIFICATAION_ID
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import com.example.covenantsermons.viewmodel.DownloadViewModel
import com.example.covenantsermons.viewmodel.SermonViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // downloadViewModel=get<DownloadViewModel>()
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.i("onCreateView called in PodcastListFragment")
        // podcastListViewModel=ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
        return inflater.inflate(R.layout.podcast_list_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val playerView=requireActivity().findViewById<View>(R.id.player_view)
//    }

    override fun onStart() {
        super.onStart()
        //    podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//        setViewModel()
//        setRVLayoutManager()
////        val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//        podcastListViewModel.getPodcastsFromDatabase()
    }

    override fun onResume() {
        super.onResume()
        val mainActivity: MainActivity? = activity as MainActivity
        mainActivity?.hideUpButton()
    }


    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//    Bundle savedInstanceState)
//    {
//        final View view = inflater.inflate(R.layout..., container, false);
//
//        Button button = getActivity().findViewById(R.id...);
//        button.setOnClickListener(...); - another problem: button is null
//
//        return view;
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // podcastListViewModel.getPodcastsFromDatabase()


        setAdapter()
        setRVLayoutManager()
        setPodcastViewModel()
        setSermonViewModel()
        setPlayerViewModel()
        //adapterOnClickListener()

//        val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
        // podcastListViewModel.getPodcastsFromDatabase()


//
//        podcastListViewModel.podcasts.observe(viewLifecycleOwner, Observer { list ->
//           //TODO create recyclerview here
//        })
//
//        podcast_list_rv.layoutManager = LinearLayoutManager(context)
//        podcast_list_rv.adapter = newAdapter
//
//        podcastListViewModel.getPodcasts()
    }

    private fun setAdapter() {
        Timber.i("setAdapter called")

        podcastAdapter = PodcastAdapter(activity, sermonArrayList).also { it ->
            it.onItemClick = { sermon ->
                //Toast.makeText(activity, "title ${sermon.title} audio file${sermon.audioFile}", Toast.LENGTH_LONG).show()
                Timber.i("on Click called title ${sermon.title}")
                Timber.i("sermon= $sermon")
                //podcast_list_rv.adapter=it
//                Timber.i("podcastListViewModel.transformLiveData() ${podcastListViewModel.transformLiveData().size}")
//                playerViewModel.play(sermon, podcastListViewModel.transformLiveData())
//                downloadViewModel.startWork(sermon)

                //TODO make sure sermon in room database before going to detail fragment
                //      otherwise toast that it needs to be downloaded first

                //TODO check podcastsDownloaded list for sermon
//                Timber.i("podcastListViewModel.podcastsDownloaded.value= ${podcastListViewModel.podcastsDownloaded.value}")
//                if(podcastListViewModel.podcastsDownloaded.value?.sermonInDownloadedList(sermon)==true) {
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
                //val sermonArrayList = arrayListOf<Sermon>(sermon)
                // downloadViewModel.setUpCurrentSermon(sermonArrayList)

                if (podcastsDownloaded.sermonInDownloadedList(sermon)) {
                    showPodcastDetailFragment(sermon)
                } else {

                    //TODO save sermon to downloadViewModel
                    sermon.date?.let { date ->
                        if (sermonViewModel.count(date) == 0) {
                            downloadViewModel.let {
                                //TODO check if already in database
                                //sermonViewModel
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

    private fun setPodcastViewModel() {
        Timber.i("setPodcastViewModel called")
        activity?.let { getPodcastsFromDatabase(podcastListViewModel, it) }
        podcastListViewModel.podcasts.observe(viewLifecycleOwner, Observer { list ->
            Timber.i("podcasts observer called")
            sermonArrayList = ArrayList(list)
            if (sermonEntityArrayList.size > 0) {
                createCombinedSermonArrayList()
            } else {
                combinedSermonArrayList = sermonArrayList
            }
            sermonListUpdated()
        })

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

//    private fun adapterOnClickListener(){
//        podcastAdapter.setOnItemClickListener { item, view ->
//            if (item is PodcastItem) {
//                val navigationExtra = FragmentNavigatorExtras(
//                        view.podcast_image to item.podcast.id
//                )
//                findNavController().navigate(
//                        R.id.action_mainFragment_to_podcastDetailsFragment,
//                        bundleOf(
//                                PodcastDetailsFragment.podcastIdArgument to item.podcast.id,
//                                PodcastDetailsFragment.podcastTitleArgument to item.podcast.name
//                        ), null, navigationExtra
//                )
//            }
//        }
//    }

    private fun swipePodcastListener(podcastRecyclerView: RecyclerView) {
        val podcastItemSwipeCallback = object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {


//            override fun getSwipeDirs (recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//                return 0
//                return super.getSwipeDirs(recyclerView, viewHolder)
//            }

            //            val trashBinIcon = resources.getDrawable(
//                    R.drawable.ic_trash_can,
//                    null
//            )

            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) {

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

                        Timber.i("onSwiped after delete sermonEntityList= $sermonEntityList")
                        val sermonList = SermonEntity.fromSermonEntityToSermon(sermonEntityList)
                        val sermonDownloadedList = ArrayList(sermonList).sermonRemoveFromDownloadedList(sermon)
                        Timber.i("sermonDownloadedList= $sermonDownloadedList")
                        podcastListViewModel.setDownloadedPodcasts(sermonDownloadedList)
                        //activity?.let { getPodcastsFromDatabase(podcastListViewModel, it) }
                        setPodcastViewModel()
                        setSermonViewModel()
                        if (currentPlaying.date != null && sermon?.date != null) {
                            Timber.i("currentPlaying= $currentPlaying")
                            if (currentPlaying.date?.compareTo(sermon.date!!, false) == 0) {
                                playerViewModel.emptyPlayList()
                                val notificationManager=activity?.applicationContext?.let { NotificationManagerCompat.from(it) }
                                Timber.i("onSwiped notificationManager= $notificationManager")
                                notificationManager?.cancel(NOW_PLAYING_CHANNEL_ID, NOW_PLAYING_NOTIFICATAION_ID)
//                                stopForeground(PlayerService::class.java)
//                                if (Context.NOTIFICATION_SERVICE != null) {
//                                    val ns: String = Context.NOTIFICATION_SERVICE
//                                    activity?.applicationContext?.getSystemService(ns).cancel()
//                                    notification.cancel()
//                                }
//                                val notificationManager=activity?.let { NotificationManagerCompat.from(it) }
//                                val notificationManager=activity?.let { NotificationManagerCompat.from(playerService.getPlayerServiceContext()) }
//                                if (notificationManager !=null){
//                                    Timber.i("onSwipe notificationManager does not equal null")
//                                    notificationManager?.cancel(NOW_PLAYING_CHANNEL_ID, NOW_PLAYING_NOTIFICATAION_ID)
//                                }

                            }
                        }

                        if (sermonDownloadedList.size > 0) {
                            Timber.i("if sermonDownloadedList.size= ${sermonDownloadedList.size}")
                            playerViewModel.createPlaylist(null, sermonDownloadedList.toList())
                        } else {
                            Timber.i("else sermonDownloadedList.size= ${sermonDownloadedList.size}")
                        }
                        setPodcastViewModel()
                        setSermonViewModel()
                    } else {
                        Toast.makeText(activity, "Can't remove undownloaded sermon= $sermon", Toast.LENGTH_SHORT).show()
                        Timber.i("Can't remove undownloaded sermon= $sermon")
                        sermonListUpdated()
                    }
                }
            }

            override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
            ) {

                // val foregroundView: View = (viewHolder as PodcastAdapter.ItemViewHolder).itemView

//                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
//                        actionState, isCurrentlyActive)

                c.clipRect(0f, viewHolder.itemView.top.toFloat(),
                        dX, viewHolder.itemView.bottom.toFloat())



                super.onChildDraw(c, recyclerView, viewHolder,
                        dX, dY, actionState, isCurrentlyActive)
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


    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
//            val sermon=sermonArrayList[adapterPosition]
//            itemView.audioFile
//            itemView.

//            var audioFile: String? = "",
//            var duration: Int? = 0,
//            var image: String? = "",
//            var pastorName: String? = "",
//            var timeStamp: Date? = Date(),
//            var title: String? = ""

//                val navigationExtra = FragmentNavigatorExtras(
//                        view.podcast_image to item.podcast.id
//                )
//                findNavController().navigate(
//                        R.id.action_mainFragment_to_podcastDetailsFragment,
//                        bundleOf(
//                                PodcastDetailsFragment.podcastIdArgument to item.podcast.id,
//                                PodcastDetailsFragment.podcastTitleArgument to item.podcast.name
//                        ), null, navigationExtra
//                )
        }
        return this
    }
}

//}