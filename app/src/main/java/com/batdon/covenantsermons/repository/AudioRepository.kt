package com.batdon.covenantsermons.repository

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.KoinComponent
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class AudioRepository(val mContext: Context): Repository, CoroutineScope, KoinComponent {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun cancelAudioJob(){
        job.cancel()
    }

    override fun registerLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun cancelJob() {
        Timber.i("cancelJob called")
        if (job.isActive) {
            Timber.i("job is active coroutine still fetching image")
            Timber.i("about to cancel job")
            job.cancel()
        }
    }
}