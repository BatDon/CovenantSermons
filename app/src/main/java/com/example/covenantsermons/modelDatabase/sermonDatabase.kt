package com.example.covenantsermons.modelDatabase

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.example.covenantsermons.*
import com.example.covenantsermons.extensions.timeStampToDate
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.player.PodcastListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*


data class SermonList(
        val sermonList: ArrayList<Sermon> = arrayListOf()
)



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


    fun authenticateAnonymousUser(podcastListViewModel: PodcastListViewModel, mContext: Context){

        val auth=Firebase.auth
        auth.signInAnonymously()
                .addOnCompleteListener(mContext as Activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        val user = auth.currentUser
                        getPodcastsFromDatabase(podcastListViewModel, mContext)
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.i("unable to sign in anonymously")
                    }
                }
    }

    private fun getPodcastsFromDatabase(podcastListViewModel: PodcastListViewModel, mContext: Context) {

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
                        val pastorName: String? = sermondChanges.document.data["pastorName"] as String?
                        //val audioFileCompletePath: String? = sermondChanges.document.data["audioFile"] as String?
//                        val audioFile= audioFileCompletePath?.httpsRefToStorageRef()
                        val audioFile=sermondChanges.document.data["audioFile"] as String?
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



