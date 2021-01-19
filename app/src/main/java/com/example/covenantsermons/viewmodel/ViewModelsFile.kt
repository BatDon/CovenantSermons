package com.example.covenantsermons.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.covenantsermons.modelDatabase.Sermon

class MainViewModel : ViewModel(){
    var sermonArrayListLiveData = MutableLiveData<ArrayList<Sermon>>()

    fun addSermons(sermonArrayList: ArrayList<Sermon> ){
//        sermonArrayListLiveData.value=sermonArrayList.forEach(sermonArrayList.get())
//        sermonArrayList.forEach(
//                sermonArrayListLiveData.value=sermonArrayList
//        )
        sermonArrayListLiveData.postValue(sermonArrayList)

    }

    fun getLiveSermons()=sermonArrayListLiveData

//    fun getPodcasts(){
//        addSermons(getPodcastsFromDatabase())
//    }

//    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
//        val bundle = Bundle().apply {
//            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
////            putParcelable(SERMON_KEY, sermon)
//        }
//        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
//            putExtra(PlayerActivity.BUNDLE_KEY, bundle)
//        }
//        startActivity(intent)
//    }


}