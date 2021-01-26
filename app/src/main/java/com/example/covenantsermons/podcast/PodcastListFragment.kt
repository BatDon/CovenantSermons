package com.example.covenantsermons.podcast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covenantsermons.R
import com.example.covenantsermons.modelDatabase.Sermon
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.player.PodcastListViewModel
import kotlinx.android.synthetic.main.podcast_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


//class PodcastListFragment : Fragment(R.layout.podcast_list_fragment) {
class PodcastListFragment : Fragment() {

    //val podcastListViewModel: PodcastListViewModel by viewModel()
//    R.layout.podcast_list_fragment

    private val podcastListViewModel: PodcastListViewModel by viewModel()
//    private val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//    private lateinit var podcastListViewModel: PodcastListViewModel
    private var sermonArrayList:ArrayList<Sermon?> = ArrayList<Sermon?>()
    private lateinit var podcastAdapter: PodcastAdapter

//    companion object {
//
//        fun newInstance(): PodcastListFragment {
//            return PodcastListFragment()
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.i("onCreateView called in PodcastListFragment")
       // podcastListViewModel=ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
        return inflater.inflate(R.layout.podcast_list_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
    //    podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//        setViewModel()
//        setRVLayoutManager()
////        val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//        podcastListViewModel.getPodcastsFromDatabase()
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



//        val newAdapter = GroupAdapter<GroupieViewHolder>()
//        val topSection = Section()
//        newAdapter.add(topSection)
//
//        newAdapter.setOnItemClickListener { item, view ->
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


       // podcastListViewModel.getPodcastsFromDatabase()



        setAdapter()
        setRVLayoutManager()
        setViewModel()
//        val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
       // podcastListViewModel.getPodcastsFromDatabase()




//        podcast_list_swipe_refresh.setOnRefreshListener {
//            podcastListViewModel.getPodcasts()
//        }
//
//        podcastListViewModel.isLoading.observe(viewLifecycleOwner, Observer {
//            podcast_list_swipe_refresh.isRefreshing = it
//        })
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

        podcastAdapter = PodcastAdapter(sermonArrayList)//.also{
            //it.notifyDataSetChanged()
            //podcastAdapter.updateSermonList(sermonArrayList)
             //podcastAdapter.notifyDataSetChanged()
        //}
        podcast_list_rv.adapter = podcastAdapter
    }

    private fun setRVLayoutManager() {
        Timber.i("setRVLayoutManager called")
        val mLayoutManager = LinearLayoutManager(podcast_list_rv.context)
        podcast_list_rv.layoutManager = mLayoutManager
        podcast_list_rv.setHasFixedSize(true)
        if(sermonArrayList.size>0) {
            podcast_list_rv.visibility = View.VISIBLE
            progress_bar.visibility = View.INVISIBLE
        }
    }

    private fun setViewModel(){
        getPodcastsFromDatabase(podcastListViewModel)
        podcastListViewModel.podcasts.observe(viewLifecycleOwner, Observer { list ->
            sermonArrayList=ArrayList(list)
            sermonListUpdated()
        })
    }

    private fun sermonListUpdated(){
        podcastAdapter.updateSermonList(sermonArrayList)
        podcast_list_rv.visibility = View.VISIBLE
        progress_bar.visibility = View.INVISIBLE
    }
}