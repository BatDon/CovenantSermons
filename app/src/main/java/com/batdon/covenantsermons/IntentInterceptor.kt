package com.batdon.covenantsermons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class IntentInterceptor : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.i("getIntentInterceptor called= ${intent}")
    }
    //    override fun onCreate(){
//        Timber.plant(Timber.DebugTree())
//        Timber.i("getIntentMainActivity called= ${intent}")
//    }
}