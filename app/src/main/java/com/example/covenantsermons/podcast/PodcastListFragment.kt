package com.example.covenantsermons.podcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.MasterFragmentViewModel
import com.example.covenantsermons.R
import com.example.covenantsermons.extensions.combineSermonLists
import com.example.covenantsermons.extensions.sermonInDownloadedList
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.modelClass.SermonEntity
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import com.example.covenantsermons.viewmodel.DownloadViewModel
import com.example.covenantsermons.viewmodel.SermonViewModel
import kotlinx.android.synthetic.main.podcast_list_fragment.*
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
//    private var downloadViewModel: DownloadViewModel? =null



    //    private val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//    private lateinit var podcastListViewModel: PodcastListViewModel
    private var sermonPlayArrayList: ArrayList<Sermon?> = ArrayList<Sermon?>()
    private var sermonArrayList: ArrayList<Sermon?> = ArrayList<Sermon?>()
    private var sermonEntityArrayList: ArrayList<SermonEntity> = ArrayList<SermonEntity>()
    private var combinedSermonArrayList:ArrayList<Sermon?> =ArrayList<Sermon?>()
    private var podcastsDownloaded:ArrayList<Sermon?> =ArrayList<Sermon?>()
    private lateinit var podcastAdapter: PodcastAdapter

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
        val mainActivity:MainActivity?=activity as MainActivity
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
                if(podcastsDownloaded.sermonInDownloadedList(sermon)) {

                    Timber.i("podcastListViewModel.podcastsDownloaded.value= ${podcastListViewModel.podcastsDownloaded.value}")

                    podcastListViewModel.podcastsDownloaded.value?.let { sermonArrayList -> playerViewModel.play(sermon, sermonArrayList) }
                    findNavController().navigate(
                            R.id.action_mainFragment_to_podcastDetailsFragment,
                            bundleOf(
                                    PodcastDetailsFragment.podcastSermonArgument to sermon,
                            ), null, null)
                }else{
                    Toast.makeText(activity, "Please download first.",Toast.LENGTH_LONG).show()
                }

            }

            it.onDownloadCancelPlayClick = { sermon ->
                Timber.i("onDownloadCancelPlayClick called ${sermon.title}")
                Timber.i("onDownloadCancelPlayClick sermon= $sermon")
                //val sermonArrayList = arrayListOf<Sermon>(sermon)
               // downloadViewModel.setUpCurrentSermon(sermonArrayList)


                //TODO save sermon to downloadViewModel
                sermon.date?.let { date ->
                    if(sermonViewModel.count(date)==0) {
                        downloadViewModel.let {
                            //TODO check if already in database
                            //sermonViewModel
                            downloadViewModel.sermonArrayList.clear()
                            downloadViewModel.sermonArrayList.add(sermon)
                            it.startWork(sermon)
                        }
                    }
                    //TODO remove only for testing
                    else{
                        Timber.i("SermonEntity already exists")
                    }
                }
                //downloadViewModel.startWork(sermon)
//                //TODO pass to sermon or service so user can close app and still download
//                //val httpsReference = storage.getReferenceFromUrl(sermon.audioFile)
//            }


//                podcast_list_rv.adapter = it
            }
            podcast_list_rv.adapter = it
            //podcast_list_rv.adapter=it

//        podcast_list_rv.adapter = podcastAdapter
        }
    }

    private fun setRVLayoutManager() {
        Timber.i("setRVLayoutManager called")
        val mLayoutManager = LinearLayoutManager(podcast_list_rv.context)
        podcast_list_rv.layoutManager = mLayoutManager

        val mDividerItemDecoration = DividerItemDecoration(podcast_list_rv.context,
                mLayoutManager.orientation)
        podcast_list_rv.addItemDecoration(mDividerItemDecoration)

        podcast_list_rv.setHasFixedSize(true)
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
            if(sermonEntityArrayList.size>0){
                createCombinedSermonArrayList()
            }else{
                combinedSermonArrayList= sermonArrayList
            }
            sermonListUpdated()
        })

        podcastListViewModel.podcastsDownloaded.observe(viewLifecycleOwner, Observer { list ->
            Timber.i("podcastsDownloaded observer called")
            podcastsDownloaded=ArrayList(list)
            Timber.i("podcastsDownloaded= $podcastsDownloaded")
            if(sermonEntityArrayList.size>0){
                createCombinedSermonArrayList()
            }else{
                combinedSermonArrayList=sermonArrayList
            }
            sermonListUpdated()
        })


        masterFragmentViewModel.toShowAppBar(true)
    }

    private fun setSermonViewModel(){
        sermonViewModel.allSermons.observe(viewLifecycleOwner, Observer { list ->
            sermonEntityArrayList = ArrayList(list)
            Timber.i("sermonEntityArrayList= $sermonEntityArrayList")
            Timber.i("sermonEntityArrayList size= ${sermonEntityArrayList.size}")
            if(sermonEntityArrayList.size>0){
                createCombinedSermonArrayList()
            }else{
                combinedSermonArrayList=sermonArrayList
            }

        })
    }

    private fun createCombinedSermonArrayList(){
        combinedSermonArrayList = sermonEntityArrayList.combineSermonLists(sermonArrayList)
        sermonListUpdated()
    }

    private fun sermonListUpdated() {
        Timber.i("sermonListUpdated called")
        podcastAdapter.updateSermonList(combinedSermonArrayList)
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

    fun PodcastAdapter.ItemViewHolder.listen(event: (position: Int, type: Int) -> Unit): PodcastAdapter.ItemViewHolder {
        itemView.setOnClickListener {
            event.invoke(getAdapterPosition(), getItemViewType())
        }
        return this
    }
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

//}