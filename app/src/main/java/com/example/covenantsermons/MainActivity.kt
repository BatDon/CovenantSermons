package com.example.covenantsermons

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.covenantsermons.PlayerActivity.Companion.BUNDLE_KEY
import com.example.covenantsermons.PlayerActivity.Companion.SERMON_KEY
import com.example.covenantsermons.modelDatabase.Sermon
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        //TODO uncomment after done testing used to fill recyclerview
        getPodcastsFromDatabase()

        val sermon=Sermon("file:///home/david/PodcastAppResources/AudioFiles/krewella-come-get-it.mp3",
        63,"file:///home/david/Downloads/cross.png","pastorName",
                Date(),"sermonTitle1")

        val bundle=Bundle().apply {
            putParcelable(SERMON_KEY, sermon)
        }

        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
            putExtra(BUNDLE_KEY,bundle)
        }
        startActivity(intent)
    }

    //
    override fun onStart() {
        super.onStart()
        //TODO start service here
        //startForegroundService()
        //TODO bind to service here
    }

    //TODO unbind from service
    override fun onStop() {
        super.onStop()
    }


    //TODO uncomment after done testing
//    override fun onDestroy() {
//        super.onDestroy()
//        unSubscribe?.remove()
//    }
}