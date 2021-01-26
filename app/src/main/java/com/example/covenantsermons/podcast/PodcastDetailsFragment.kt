package com.example.covenantsermons.podcast

import androidx.fragment.app.Fragment
import com.example.covenantsermons.R

/**
 * Created by Abdurahman Adilovic on 3/22/20.
 */

class PodcastDetailsFragment : Fragment(R.layout.podcast_item) {
//    private val podcastDetailsViewModel by viewModel<PodcastDetailsViewModel>()
//    private val playerViewModel by sharedViewModel<PlayerViewModel>()
//    private lateinit var podcastId: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        podcastId = arguments?.getString(podcastIdArgument) ?: ""
//        sharedElementEnterTransition =
//            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        postponeEnterTransition()
//
//        val adapter = GroupAdapter<GroupieViewHolder>()
//
//        podcastDetailsViewModel.isLoading.observe(viewLifecycleOwner, Observer {
//            podcast_details_swipe_refresh.isRefreshing = it
//        })
//
//        podcastDetailsViewModel.podcastAndEpisodes.observe(viewLifecycleOwner, Observer { podcast ->
//            adapter.clear()
//            adapter.add(PodcastDetailsItem(podcast))
//            adapter.addAll(podcast.episodes.map { EpisodeRow(it) })
//            startPostponedEnterTransition()
//        })
//
//        podcastDetailsViewModel.errors.observe(viewLifecycleOwner, Observer {
//            context?.toastIt(it)
//        })
//
//        podcast_details_swipe_refresh.setOnRefreshListener {
//            podcastDetailsViewModel.getPodcastDetails(podcastId)
//        }
//
//        adapter.setOnItemClickListener { item, _ ->
//            if (item is EpisodeRow) {
//                val episodes = podcastDetailsViewModel.podcastAndEpisodes.value?.episodes ?: listOf(
//                    item.episode
//                )
//                playerViewModel.play(item.episode, episodes)
//            }
//        }
//
//        podcast_details_rv.adapter = adapter
//        podcast_details_rv.layoutManager = LinearLayoutManager(context)
//
//        podcastDetailsViewModel.getPodcastDetails(podcastId)
//    }
//
//    companion object {
//        const val podcastIdArgument = "podcast_id"
//        const val podcastTitleArgument = "podcast_title"
//    }

}