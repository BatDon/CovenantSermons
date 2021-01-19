package com.example.covenantsermons

import android.app.Application
import com.example.covenantsermons.modelDatabase.Sermon
import java.lang.ref.WeakReference
import java.util.*

//class MainActivityAccesser(newDataInterface: NewDataInterface):AccessViewInterface {
//
//    private val view = WeakReference(newDataInterface)
//
//    override fun getViewReference(): WeakReference<NewDataInterface> =view
//}

class MainActivityAccesser(viewInterface: ViewInterface?): Application(), AccessViewInterface  {
    private var view:WeakReference<ViewInterface>?=null

    companion object {
        lateinit var instance: MainActivityAccesser
            private set
    }

    override fun onCreate () {
        super.onCreate ()
        instance = this
    }

    init{
        if(viewInterface!=null){
            view=WeakReference(viewInterface)
        }
    }
//    private var view = WeakReference(viewInterface)
    override fun getViewReference(): WeakReference<ViewInterface>? = view
}


//class MainActivityAccesser(viewInterface: ViewInterface): MainActivityAccesser.AccessViewInterface {
//
////    private val view = WeakReference(viewInterface)

//
//    override fun getViewReference(): WeakReference<ViewInterface> =view
//}


interface NewDataInterface {
    fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>)
}

interface ViewInterface{
    interface NewDataInterface {
        fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>)
    }
}

interface AccessViewInterface{
    fun getViewReference(): WeakReference<ViewInterface>?
}

//interface NewDataInterface {
//    fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>)
//}
//
//interface ViewInterface
//
//interface AccessViewInterface{
//    fun getViewReference(): WeakReference<NewDataInterface>
//}