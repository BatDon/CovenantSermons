package com.example.covenantsermons.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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
    val title: String = ""
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
}


//constant val

fun getPodcastsFromDatabase(){

    var rootRef:FirebaseFirestore? = FirebaseFirestore.getInstance()



    rootRef?.let {
        it.collection("Podcasts").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Timber.i("document id= ${document.id} => ${document.data}")
                        val audioFile=document.getString(documentFields.audioFile)
                        val duration=document.getLong(documentFields.duration)
                        val image=document.getString(documentFields.image)
                        val timeStamp=document.getDate(documentFields.timeStamp)
                        val title=document.getString(documentFields.title)

                        //testing all field values
                        Timber.i("""audioFile $audioFile
                                             duration $duration
                                             image    $image
                                             timeStamp $timeStamp
                                             title    $title""")
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.i("Error getting documents: $exception")
                }
    }



}

