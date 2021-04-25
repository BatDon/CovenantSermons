package com.batdon.covenantsermons.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageRepository(val mContext: Context): Repository, CoroutineScope, KoinComponent {

    private val currentSermonImage = MutableLiveData<Bitmap>()

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var bitmap:Bitmap?=null




//    fun getSermonImage(){
//        coroutineContext.launch {
//            call()
//        }
//    }

  //  override fun getSermonImage(url: String): LiveData<Bitmap> {
    fun getSermonImage(url: String): Bitmap?{
      Timber.i("getSermonImage called url= $url")
      var bitmap:Bitmap?=null
//        launch{
      runBlocking(coroutineContext){
            bitmap=call(url)
            currentSermonImage.value = bitmap!!
//        coroutineScope.launch {
//            call()
        }
        //return currentSermonImage
        Timber.i("return bitmap called bitmap=$bitmap")
        return bitmap
    }



    //private suspend fun call(url: String){
//    private suspend fun call(url: String)=withContext(Dispatchers.IO){
    private suspend fun call(url: String): Bitmap? =withContext(Dispatchers.IO){
        Timber.i("call function called")
        val result= url.getBitmapFromGlide()
        Timber.i("result= $result")
        //val result = database.getReference("ref").singleValueEvent()
        when(result) {
            is EventResponse.ResourceReady -> {
                val bitmap = result.bitmap
//                currentSermonImage.value = bitmap
                Timber.i("call function called bitmap= $bitmap")
//                this.bitmap = bitmap
                return@withContext bitmap

            }
            is EventResponse.LoadCleared -> {
                Timber.i("EventResponse.LoadCleared called")
                val drawable = result.drawable
            }

        }
        return@withContext null

    }



    sealed class EventResponse {
        data class ResourceReady(val bitmap: Bitmap): EventResponse()
        data class LoadCleared(val drawable: Drawable): EventResponse()

        companion object {
            fun resourceReady(bitmap: Bitmap): EventResponse = ResourceReady(bitmap)
            fun loadCleared(drawable: Drawable): EventResponse = LoadCleared(drawable)
        }
    }



        suspend fun String.getBitmapFromGlide(): EventResponse = suspendCoroutine { continuation ->
            Timber.i("getBitmapFromGlide called")
            val bitmapEventListener= object: CustomTarget<Bitmap?>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                    Timber.i("onResourceReady called resource= $resource")
                    continuation.resume(EventResponse.resourceReady(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Timber.i("onLoadCleared called")
                    placeholder?.let { EventResponse.loadCleared(it) }?.let { continuation.resume(it) }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Timber.i("load failed")
                    // Intentionally empty, this can be optionally implemented by subclasses.
                }

            }
//            if(this==null){
//                Timber.i("this equals null")
//                Glide.with(mContext).asBitmap().load(R.drawable.cross).into(bitmapEventListener)
//            }

            Glide.with(mContext).asBitmap().load(this).into(bitmapEventListener)
        }



    fun cancelImageJob(){
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