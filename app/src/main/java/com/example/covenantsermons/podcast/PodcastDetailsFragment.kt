package com.example.covenantsermons.podcast

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.covenantsermons.MainActivity
import com.example.covenantsermons.MasterFragmentViewModel
import com.example.covenantsermons.databinding.PodcastDetailFragmentBinding
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.player.PlayerViewModel
import com.example.covenantsermons.player.PodcastListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


class PodcastDetailsFragment : Fragment() {
//class PodcastDetailsFragment : Fragment(R.layout.podcast_detail_fragment) {
//    private val podcastDetailsViewModel:PodcastDetailsViewModel by viewModel()
    private val playerViewModel: PlayerViewModel by sharedViewModel()
    private val podcastListViewModel: PodcastListViewModel by viewModel()
    private val masterFragmentViewModel: MasterFragmentViewModel by sharedViewModel()
    private var sermon: Sermon?= null
    private var podcastDetailFragmentBinding: PodcastDetailFragmentBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sermon = (arguments?.getParcelable<Sermon>(podcastSermonArgument) ?: Sermon())
        Timber.i("sermon from arguments= $sermon")

        playerViewModel.currentlyPlaying.observe(this, Observer { sermon ->
//            this.sermon = sermon
            this@PodcastDetailsFragment.sermon = sermon
            Timber.i("this@PodcastDetailsFragment.sermon= $sermon")
            setUpFragmentViews()

//            activityMainBinding.currentSermonTitle.text = sermon.title
//            Timber.i("sermon Title changed")
//            Timber.i("sermon.title is ${sermon.title}")
        })
        //Toast.makeText(activity, "title ${sermon.title} audio file${sermon.audioFile}", Toast.LENGTH_LONG).show()



//        sharedElementEnterTransition =
//            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.i("onCreateView called")
        podcastDetailFragmentBinding= PodcastDetailFragmentBinding.inflate(layoutInflater)

        return podcastDetailFragmentBinding!!.root

    }

    override fun onResume() {
        super.onResume()
        val mainActivity:MainActivity? = activity as MainActivity
        mainActivity?.showUpButton()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated called")
//        val mainActivity=activity as MainActivity
//        mainActivity.let {
//
//            it.showUpButton()
////            it.activityMainBinding.collapsingToolbar.visibility=GONE
////            it.findViewById<View>(R.id.app_bar_collapsing_toolbar).visibility = GONE
////            it.findViewById<View>(R.id.toolbar).visibility = VISIBLE
//        }
        masterFragmentViewModel.toShowAppBar(false)

        setUpFragmentViews()

//        val binding = PodcastDetailFragmentBinding.bind(view)
//        podcastDetailFragmentBinding = binding

//        val base64= sermon.image?.let { stringToBase64(it) }
//        val bitmap: Bitmap?=stringToBitmap(base64)


//        if (item is EpisodeRow) {
//            val episodes = podcastDetailsViewModel.podcastAndEpisodes.value?.episodes ?: listOf(
//                    item.episode
//            )
            //val sermonList=podcastListViewModel.podcasts

        //TODO uncomment only for testing
//            playerViewModel.play(sermon, podcastListViewModel.transformLiveData())
        }

    private fun setUpFragmentViews(){
        loadImageFromFile()
        //activity?.applicationContext?.let { podcastDetailFragmentBinding?.let { podcastDetailView -> Glide.with(it).load(sermon?.image).error(R.drawable.cross).into(podcastDetailView.sermonImageIv) } }
//        podcastDetailFragmentBinding?.sermonImageIv!!.setImageBitmap(bitmap)
        sermon?.let{
            podcastDetailFragmentBinding?.sermonTitleTv!!.text=it.title
            podcastDetailFragmentBinding?.sermonPastorNameTv!!.text=it.pastorName
            Timber.i("sermon.title ${it.title}")
            Timber.i("sermon.pastorName ${it.pastorName}")
        }
    }

    private fun loadImageFromFile(){
        try {
//            val f: File = File(activity?.filesDir.toString() + "/"+sermon?.image, sermon?.image?.pathToName()+".jpeg")
//            val f: File = File(activity?.createRootStoragePath() +sermon?.image, sermon?.image?.pathToName()+".jpeg")
    //TODO same method in PlayerService make an extension fun
            sermon.let{
                sermon?.image.let{
                    val sermonImage=sermon!!.image!!
                    val file: File = File(sermonImage)
                    val b = BitmapFactory.decodeStream(FileInputStream(file))
                    Timber.i("bitmap= $b")
                    val img: ImageView? = podcastDetailFragmentBinding?.sermonImageIv
                    img?.setImageBitmap(b)
                }
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }










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
//            podcastDetailsViewModel.getPodcastDetails(podcastSermon)
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
//        podcastDetailsViewModel.getPodcastDetails(podcastSermon)
//    }

    fun stringToBase64(imagePath: String?):String{
        val bytes = File(imagePath).readBytes()
        val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
        //android.util.Base64.decode(imagePath, android.util.Base64.DEFAULT);
        return base64
    }

    fun stringToBitmap(encodedString: String?): Bitmap? {
        //val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)
        val imageBytes=android.util.Base64.decode(encodedString, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//        val imageBytes = Base64.decode(encodedString, 0)
//        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun onStop() {
        super.onStop()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//        (activity as AppCompatActivity?)!!.appBar!!.visibility=VISIBLE
//        val mainActivity=activity as MainActivity
//        mainActivity.let {
//            it.activityMainBinding.appBarCollapsingToolbar.appBar.visibility = VISIBLE
//            it.activityMainBinding.toolbar.visibility = GONE
//            it.findViewById<View>(R.id.app_bar_collapsing_toolbar).visibility=VISIBLE
//            it.findViewById<View>(R.id.toolbar).visibility= GONE
//        }
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field
        // if not needed.
        podcastDetailFragmentBinding = null
        super.onDestroyView()
    }

    companion object {
        const val podcastSermonArgument = "podcast_sermon"
        //const val podcastTitleArgument = "podcast_title"
    }

}