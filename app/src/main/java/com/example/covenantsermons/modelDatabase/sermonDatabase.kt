package com.example.covenantsermons.modelDatabase

import android.content.Context
import android.os.Parcelable
import com.example.covenantsermons.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.parcel.Parcelize
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

//@Parcelize
//data class Sermon(
//    val audioFile: String = "",
//    val duration: Int = 0,
//    val image: String = "",
//    val timeStamp: Date = Date(),
//    val title: String = "",
//    val pastorName: String=""
//): Parcelable

@Parcelize
data class Sermon(
    var audioFile: String? = "",
    var duration: Int? = 0,
    var image: String? = "",
    var pastorName: String? = "",
    var timeStamp: Date? = Date(),
    var title: String? = ""

): Parcelable{
    init {
        audioFile = audioFile ?: ""
        duration = duration?:0
        image = image ?: ""
        pastorName = pastorName ?: ""
        timeStamp = timeStamp ?: Date()
        title = title ?: ""


    }
}


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

fun getPodcastsFromDatabase(mContext: Context): ArrayList<Sermon>{

//    val mainContext=mainContext

    var rootRef:FirebaseFirestore? = FirebaseFirestore.getInstance()

//    var unSubscribe: ListenerRegistration?=null

    var sermonList=ArrayList<Sermon>()

    unSubscribe=rootRef?.collection("Podcasts")
            ?.addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    Timber.e( "listen:error $exception")
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
                        DocumentChange.Type.MODIFIED -> Timber.i( "Modified sermon: ${sermondChanges.document.data}")
                        DocumentChange.Type.REMOVED -> Timber.i( "Removed sermon ${sermondChanges.document.data}")
                        //sermondChanges.document.data.get("audioFile")

                    }
                    Timber.i("sermon Media= ${sermondChanges.document.data["audioFile"]}")
                    val audioFile: String?= sermondChanges.document.data["audioFile"] as String?
                    val durationLong: Long?= sermondChanges.document.data["duration"] as Long?
                    val duration: Int? =durationLong?.toInt()
//                    val duration: Int? =sermondChanges.document.data["duration"].toInt()
                    val image: String?= sermondChanges.document.data["image"] as String?
                    val pastorName: String?= sermondChanges.document.data["pastor"] as String?
                    val timeStampString: String=sermondChanges.document.data["timeStamp"].toString()
//                    val timeStamp: Timestamp=sermondChanges.document.data["timeStamp"] as Timestamp
//                    val longTimeStamp: Long=timeStamp.seconds.toLong()
                    Timber.i("timeStampString = $timeStampString")
                    val firstTimeStampSubstring=timeStampString.substringAfter("=",timeStampString)
                    val lastTimeStampSubstring=firstTimeStampSubstring.substringBefore(",",firstTimeStampSubstring)
//                    Timber.i("substring= $lastTimeStampSubstring")
                    val date= Date(lastTimeStampSubstring.toLong()*1000)
//                    val date=Date()
//                    val timeStamp: Date?= timeStampString?.toLongOrNull()?.let { Date(it) }
//                    Timber.i("date= ${longTimeStamp}")
//                    Timber.i("timeStamp=$timeStamp")
                    val title: String?= sermondChanges.document.data["title"] as String?

                    sermonList.add(Sermon(audioFile,duration, image, pastorName, date, title))
                    Timber.i("sermonList= ${sermonList[0]}")
                }
                //DatabaseListenerClass().playPodcastActivitywithNewData(sermonList)

                val mainActivityContext=mContext as MainActivity
                mainActivityContext.playPodcastActivitywithNewData(sermonList)
//                AccessViewInterface.playPodcastActivityNewData(sermonList)
//                MainActivityAccesser(null).getViewReference()

            }

    return sermonList


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



}
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


