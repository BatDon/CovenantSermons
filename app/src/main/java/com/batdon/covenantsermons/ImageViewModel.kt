package com.batdon.covenantsermons

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.batdon.covenantsermons.repository.Repository
import org.koin.core.KoinComponent

class ImageViewModel(private val repository: Repository) : ViewModel() , KoinComponent {
//class ImageViewModel(private val repository: Repository) : ViewModel() {

    //use coroutine to get image
    val _currentSermonImage= MutableLiveData<Bitmap>()
    val currentSermonImage
        get() = _currentSermonImage

//    fun getImage(url: String): LiveData<Bitmap> {
//        return repository.getSermonImage(url)
//    }

//    fun getLoadingImage(): LiveData<String> {
//        return repository.getLoadingImage()
//    }
}