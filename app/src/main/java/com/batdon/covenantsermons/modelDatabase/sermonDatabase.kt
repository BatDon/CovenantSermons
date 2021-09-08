package com.batdon.covenantsermons.modelDatabase

import android.app.Activity
import android.content.Context
import com.batdon.covenantsermons.*
import com.batdon.covenantsermons.extensions.timeStampToDate
import com.batdon.covenantsermons.modelClass.Sermon
import com.batdon.covenantsermons.player.PodcastListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*



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

        val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance()
        val sermonList = ArrayList<Sermon>()

        unSubscribe = rootRef.collection("Podcasts")
                .addSnapshotListener { snapshots, exception ->
                    if (exception != null) {
                        Timber.e("listen:error $exception")
                        return@addSnapshotListener
                    }

                    for (sermondChanges in snapshots!!.documentChanges) {
                        when (sermondChanges.type) {
                            DocumentChange.Type.ADDED -> Timber.i("New sermon: ${sermondChanges.document.data}")
                            DocumentChange.Type.MODIFIED -> Timber.i("Modified sermon: ${sermondChanges.document.data}")
                            DocumentChange.Type.REMOVED -> Timber.i("Removed sermon ${sermondChanges.document.data}")

                        }
                        Timber.i("sermon Media= ${sermondChanges.document.data["audioFile"]}")
                        val docData=sermondChanges.document.data

                        val date = docData[documentFields.timeStamp].toString().timeStampToDate()
                        val title: String? = docData[documentFields.title] as String?
                        val pastorName: String? = docData[documentFields.pastorName] as String?
                        val audioFile=docData[documentFields.audioFile] as String?
                        val duration: Int? = (docData[documentFields.duration] as Long?)?.toInt()
                        val image: String? = docData[documentFields.image] as String?

                        sermonList.add(Sermon(date, title, pastorName, audioFile, duration, image))
                        Timber.i("sermonList= ${sermonList[0]}")
                    }

                    Timber.i("sermonList size= ${sermonList.size}")
                    for (sermon in sermonList) {
                        Timber.i("sermonDatabase sermon= $sermon")
                    }

                    podcastListViewModel.setPodcasts(sermonList)
                }
}
