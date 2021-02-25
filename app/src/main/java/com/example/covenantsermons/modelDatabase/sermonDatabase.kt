package com.example.covenantsermons.modelDatabase

import android.content.Context
import android.net.ConnectivityManager
import com.example.covenantsermons.*
import com.example.covenantsermons.extensions.timeStampToDate
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.player.PodcastListViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber
import java.util.*


data class SermonList(
        val sermonList: ArrayList<Sermon> = arrayListOf()
)


//data class Sermon(
//    val audioFile: String = "",
//    val duration: Int = 0,
//    val image: String = "",
//    val timeStamp: Date = Date(),
//    val title: String = "",
//    val pastorName: String=""
//)
//
//@Parcelize
//data class Sermon(
//    val audioFile: String = "",
//    val duration: Int = 0,
//    val image: String = "",
//    val timeStamp: Date = Date(),
//    val title: String = "",
//    val pastorName: String=""
//): Parcelable

//@Parcelize
//data class Sermon(
//        var audioFile: String? = "",
//        var duration: Int? = 0,
//        var image: String? = "",
//        var pastorName: String? = "",
//        var date: String? = "",
//        var title: String? = ""
//
//): Parcelable{
//    init {
//        audioFile = audioFile ?: ""
//        duration = duration?:0
//        image = image ?: ""
//        pastorName = pastorName ?: ""
//        date = date ?: ""
//        title = title ?: ""
//
//    }
//}

//@Parcelize
//data class Sermon(
//    var date: String? = "",
//    var title: String? = "",
//    var pastorName: String? = "",
//    var audioFile: String? = "",
//    var duration: Int? = 0,
//    var image: String? = ""
//
//
//
//): Parcelable{
//    init {
//        date = date ?: ""
//        title = title ?: ""
//        pastorName = pastorName ?: ""
//        audioFile = audioFile ?: ""
//        duration = duration?:0
//        image = image ?: ""
//    }
//}


//enum class DocumentFields{
//    audioFile, duration, image, timeStamp, title
//}

object documentFields{
    const val audioFile="audioFile"
    const val duration="duration"
    const val image="image"
    const val timeStamp="timeStamp"
    const val title="title"
    const val pastorName="pastorName"
}

var unSubscribe: ListenerRegistration?=null
//constant val

//class SermonDatabase: AppCompatActivity(){
//class SermonDatabase{
    //lateinit var podcastListViewModel: PodcastListViewModel
   // var podcastListViewModel: PodcastListViewModel?=null
//    val podcastListViewModel: PodcastListViewModel by viewModel()


    // init{
//        var podcastListViewModel: PodcastListViewModel?=null
    //    podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//    }
    //val podcastListViewModel: PodcastListViewModel by viewModel()
//    val podcastListViewModel = ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
  //  var podcastListViewModel: PodcastListViewModel?=null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Timber.i("onCreate called")
//        //podcastListViewModel: PodcastListViewModel by viewModel()
//        podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//
//        Timber.i("onCreate called podcastListViewModel initialized to $podcastListViewModel")
//        //val podcastListViewModel=PodcastListViewModel by viewModel()
////        val podcastListViewModel: PodcastListViewModel by viewModel()
//    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Timber.i("onCreate called")
//        //podcastListViewModel: PodcastListViewModel by viewModel()
//        podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
//
//        Timber.i("onCreate called podcastListViewModel initialized to $podcastListViewModel")
//        //val podcastListViewModel=PodcastListViewModel by viewModel()
////        val podcastListViewModel: PodcastListViewModel by viewModel()
//    }



//fun getPodcastsFromDatabase(mContext: Context): ArrayList<Sermon> {
    fun getPodcastsFromDatabase(podcastListViewModel: PodcastListViewModel, mContext: Context) {

//    if (checkNetworkConnection(mContext)) {
    if (true) {


        //    val mainContext=mainContext

        var rootRef: FirebaseFirestore? = FirebaseFirestore.getInstance()

        //    var unSubscribe: ListenerRegistration?=null

        var sermonList = ArrayList<Sermon>()

        unSubscribe = rootRef?.collection("Podcasts")
                ?.addSnapshotListener { snapshots, exception ->
                    if (exception != null) {
                        Timber.e("listen:error $exception")
                        return@addSnapshotListener
                    }

                    //                new arraylist created everytime document changes
                    //                if(snapshots!!.documentChanges.size>0){
                    //                    sermonList=ArrayList<Sermon>()
                    //                }


                    //TODO create arrayList and update RecyclerView
                    for (sermondChanges in snapshots!!.documentChanges) {
                        when (sermondChanges.type) {
                            DocumentChange.Type.ADDED -> Timber.i("New sermon: ${sermondChanges.document.data}")
                            DocumentChange.Type.MODIFIED -> Timber.i("Modified sermon: ${sermondChanges.document.data}")
                            DocumentChange.Type.REMOVED -> Timber.i("Removed sermon ${sermondChanges.document.data}")
                            //sermondChanges.document.data.get("audioFile")

                        }
                        Timber.i("sermon Media= ${sermondChanges.document.data["audioFile"]}")
                        val timeStampString: String = sermondChanges.document.data["timeStamp"].toString()
                        val date = timeStampString.timeStampToDate()
                        val title: String? = sermondChanges.document.data["title"] as String?
                        val pastorName: String? = sermondChanges.document.data["pastor"] as String?
                        val audioFile: String? = sermondChanges.document.data["audioFile"] as String?
                        val durationLong: Long? = sermondChanges.document.data["duration"] as Long?
                        val duration: Int? = durationLong?.toInt()
                        //                    val duration: Int? =sermondChanges.document.data["duration"].toInt()
                        val image: String? = sermondChanges.document.data["image"] as String?



                        //                    val timeStamp: Timestamp=sermondChanges.document.data["timeStamp"] as Timestamp
                        //                    val longTimeStamp: Long=timeStamp.seconds.toLong()
                        Timber.i("timeStampString = $timeStampString")
                        //                        val firstTimeStampSubstring = timeStampString.substringAfter("=", timeStampString)
                        //                        val lastTimeStampSubstring = firstTimeStampSubstring.substringBefore(",", firstTimeStampSubstring)
                        //                        Timber.i("substring= $lastTimeStampSubstring")
                        //val date = Date(lastTimeStampSubstring.toLong() * 1000)
                        //                    val date=Date()
                        //                    val timeStamp: Date?= timeStampString?.toLongOrNull()?.let { Date(it) }
                        //                    Timber.i("date= ${longTimeStamp}")
                        //                    Timber.i("timeStamp=$timeStamp")


                        sermonList.add(Sermon(date, title, pastorName, audioFile, duration, image))
                        Timber.i("sermonList= ${sermonList[0]}")

                        //                        if(podcastListViewModel==null){
                        //                            podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
                        //                        }
                    }
                    //DatabaseListenerClass().playPodcastActivitywithNewData(sermonList)

                    //val mainActivityContext = mContext as MainActivity
                    //  mainActivityContext.playPodcastActivitywithNewData(sermonList)
                    //TODO saving as ArrayList to mutableList

                    //                    val podcastListViewModel: PodcastListViewModel by viewModel()

                    //Timber.i("podcastListViewModel $podcastListViewModel")

                    //                    if(podcastListViewModel==null){
                    //                        podcastListViewModel= ViewModelProviders.of(this).get(PodcastListViewModel::class.java)
                    //                    }

                    //Timber.i("podcastListViewModel after second initialization $podcastListViewModel")
                    Timber.i("sermonList size= ${sermonList.size}")
                    for (sermon in sermonList) {
                        Timber.i("sermonDatabase sermon= $sermon")
                    }

                    //TODO save sermonList to SharedPreferences
                    podcastListViewModel.setPodcasts(sermonList)

                    //                    if(sermonList.size>1) {
                    //                        playerViewModel.playlist.clear()
                    //                        playerViewModel.playlist.addAll(sermonList)
                    //                    }


                    //                AccessViewInterface.playPodcastActivityNewData(sermonList)
                    //                MainActivityAccesser(null).getViewReference()

                }
    } else {
        Timber.i("never happens")
    }
}

 //   return sermonList
 //   }


    fun checkNetworkConnection(mContext: Context ): Boolean {
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager!!.activeNetworkInfo != null && connectivityManager!!.activeNetworkInfo!!.isConnected
    }


//    rootRef?.let {
//        it.collection("Podcasts").get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        Timber.i("document id= ${document.id} => ${document.data}")
//                        val audioFile=document.getString(documentFields.audioFile)
//                        val duration=document.getLong(documentFields.duration)
//                        val image=document.getString(documentFields.image)
//                        val timeStamp=document.getDate(documentFields.timeStamp)
//                        val title=document.getString(documentFields.title)
//                        val pastorName=document.getString(documentFields.pastorName)
//
//                        //testing all field values
//                        Timber.i("""audioFile $audioFile
//                                             duration $duration
//                                             image    $image
//                                             timeStamp $timeStamp
//                                             title    $title
//                                             pastorName $pastorName""")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Timber.i("Error getting documents: $exception")
//                }
//    }



//    override fun onDestroy(){
//        unSubscribe?.remove()
//    }



//}
//
//class DatabaseSermonAccess(val sermonList: ArrayList<Sermon>):NewDataInterface{
//    init {
//        MainActivityAccesser(this)
//        InterfaceClass()
//        playPodcastActivitywithNewData(sermonList)
//    }
////    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
////        TODO("Not yet implemented")
////    }
//
//}


