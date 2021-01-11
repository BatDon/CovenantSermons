package com.example.covenantsermons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.covenantsermons.modelDatabase.getPodcastsFromDatabase
import com.example.covenantsermons.modelDatabase.unSubscribe
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        getPodcastsFromDatabase()
    }


    override fun onDestroy() {
        super.onDestroy()
        unSubscribe?.remove()
    }
}