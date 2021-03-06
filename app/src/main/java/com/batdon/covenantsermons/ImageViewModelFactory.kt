package com.batdon.covenantsermons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.batdon.covenantsermons.repository.Repository

class ImageViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            return ImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}