package com.example.covenantsermons

import android.os.Bundle
import com.example.covenantsermons.modelDatabase.Sermon
import java.util.*

class InterfaceClass : NewDataInterface{

    private val mainActivity=MainActivity()

    override fun playPodcastActivitywithNewData(sermonArrayList: ArrayList<Sermon>) {
        val bundle = Bundle().apply {
            putParcelable(PlayerActivity.SERMON_KEY, sermonArrayList[0])
//            putParcelable(SERMON_KEY, sermon)
        }

        mainActivity.createIntent(bundle)

    }

}
