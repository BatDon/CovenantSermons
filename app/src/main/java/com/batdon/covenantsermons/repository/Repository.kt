package com.batdon.covenantsermons.repository

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

interface Repository : LifecycleObserver {
    //fun getSermonImage(url:String): LiveData<Bitmap>
   // fun getSermonImage(url:String): Bitmap?
    fun registerLifecycle(lifecycle: Lifecycle)
}
