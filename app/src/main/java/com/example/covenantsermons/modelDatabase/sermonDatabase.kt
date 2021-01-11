package com.example.covenantsermons.modelDatabase

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


data class SermonList(
    val sermonList: ArrayList<Sermon> = arrayListOf()
)

data class Sermon(
    val audioFile: String = "",
    val duration: Int = 0,
    val image: String = "",
    val timeStamp: Date = Date(),
    val title: String = "",
    val pastorName: String=""
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
//constant val

fun getPodcastsFromDatabase(){

    var rootRef:FirebaseFirestore? = FirebaseFirestore.getInstance()

//    var unSubscribe: ListenerRegistration?=null



    unSubscribe=rootRef?.collection("Podcasts")
            ?.addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    Timber.e( "listen:error $exception")
                    return@addSnapshotListener
                }

                //TODO create arrayList and update RecyclerView
                for (sermondChanges in snapshots!!.documentChanges) {
                    when (sermondChanges.type) {
                        DocumentChange.Type.ADDED -> Timber.i("New sermon: ${sermondChanges.document.data}")
                        DocumentChange.Type.MODIFIED -> Timber.i( "Modified sermon: ${sermondChanges.document.data}")
                        DocumentChange.Type.REMOVED -> Timber.i( "Removed sermon ${sermondChanges.document.data}")
                    }
                }
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



}

