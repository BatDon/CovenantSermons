package com.batdon.covenantsermons

import android.app.Application
import com.batdon.covenantsermons.di.koinModule
import com.batdon.covenantsermons.di.presentationModule
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

