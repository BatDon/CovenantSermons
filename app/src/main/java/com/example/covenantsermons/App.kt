package com.example.covenantsermons

import android.app.Application
import com.example.covenantsermons.di.koinModule
import com.example.covenantsermons.di.presentationModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@App)
            modules(listOf(koinModule, presentationModule))
        }
    }
}

