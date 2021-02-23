package com.example.covenantsermons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    override fun getSermonImage(url: String): Bitmap?{
      Timber.i("getSermonImage called url= $url")
        launch{
            call(url)
//        coroutineScope.launch {
//            call()
        }
        //return currentSermonImage
        return bitmap
    }



    private suspend fun call(url: String){
        Timber.i("call function called")
        val result=url.getBitmapFromGlide()
        Timber.i("result= $result")
        //val result = database.getReference("ref").singleValueEvent()
        when(result) {
            is EventResponse.ResourceReady -> {
                val bitmap = result.bitmap
                currentSermonImage.value = bitmap
                Timber.i("call function called bitmap= $bitmap")
                this.bitmap = bitmap

            }
            is EventResponse.LoadCleared -> {
                Timber.i("EventResponse.LoadCleared called")
                val drawable = result.drawable
            }

        }

    }



    sealed class EventResponse {
        data class ResourceReady(val bitmap: Bitmap): EventResponse()
        data class LoadCleared(val drawable: Drawable): EventResponse()

        companion object {
            fun resourceReady(bitmap: Bitmap): EventResponse = ResourceReady(bitmap)
            fun loadCleared(drawable: Drawable): EventResponse = LoadCleared(drawable)
        }
    }

//    suspend fun DatabaseReference.singleValueEvent(): EventResponse = suspendCoroutine { continuation ->
//        val valueEventListener = object: ValueEventListener {
//            override fun onCancelled(error: DatabaseError) {
//                continuation.resume(EventResponse.Cancelled(error))
//            }
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                continuation.resume(EventResponse.Changed(snapshot))
//            }
//        }
//        addListenerForSingleValueEvent(valueEventListener) // Subscribe to the event
//    }

//    suspend fun glideCreateBitmap(url: String): EventResponse {
//
//        Bitmap.getBitmapFromGlide():
//    }

        suspend fun String.getBitmapFromGlide(): EventResponse= suspendCoroutine { continuation ->
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






    //    suspend fun glideCreateBitmap(url: String): Bitmap? =withContext(Dispatchers.IO) {
//
//            var bitmap: Bitmap? = null
//
//            var futureTargetBitmap: FutureTarget<Bitmap>
//
//            Glide.with(this@PlayerService).asBitmap().load(url).into(object : CustomTarget<Bitmap?>(){
//
//
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                    bitmap = resource
//                    Timber.i("onResourceReady bitmap= $bitmap")
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//
//            return@withContext bitmap
//        }















//    private suspend fun getImage(url: String): Bitmap? {
//
//        var bitmap: Bitmap? = null
//
//        var futureTargetBitmap: FutureTarget<Bitmap>
//
//        var lmda = null
//
////        fun foo(lmbda: (bitmap:Bitmap) -> Bitmap){
////            lmbda()
////        }
//
//        //bigIconBitmap=withContext(Dispatchers.IO){ Glide.with(this@PlayerService).asBitmap().load(url).into(object : CustomTarget<Bitmap?>(){
//        val bigIconBitmap = withContext(Dispatchers.IO) {
//            val requestBuilderBitmap: RequestBuilder<Bitmap?> = Glide.with(this@ImageRepository).asBitmap().load(url)
//            requestBuilderBitmap.into(PlayerService.CustomTargetObject)
//            while (PlayerService.CustomTargetObject.bitmapCustom == null) {
//                continue
//            }
//            return@withContext PlayerService.CustomTargetObject.bitmapCustom
//        }
//        if(bigIconBitmap!=null){
//            currentSermonImage.value=bigIconBitmap
//        }
//        return bigIconBitmap
//    }

//    override fun getPhotos(): LiveData<Bitmap> {
//        GlobalScope.launch(Dispatchers.Main) {
////                                val bitmap:Bitmap? =  sermon?.image?.let { glideCreateBitmap(it) }
//            val bitmapFromGlide: Bitmap? = sermon?.image?.let { glideCreateBitmap(it) }
//            // setBitmapForIcon(bitmap)
//
//            // return@launch bitmap
//
//        }
//    }

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