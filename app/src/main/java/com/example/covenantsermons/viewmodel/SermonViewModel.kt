package com.example.covenantsermons.viewmodel

import androidx.lifecycle.*
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.modelClass.SermonEntity
import com.example.covenantsermons.repository.SermonRepository
import kotlinx.coroutines.launch

class SermonViewModel(private val repository: SermonRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allSermons: LiveData<List<SermonEntity>> = repository.allSermons.asLiveData()

    fun insert(sermonEntity: SermonEntity) = viewModelScope.launch {
        repository.insert(sermonEntity)
    }

    fun delete(sermonEntity: SermonEntity ) = viewModelScope.launch {
        repository.delete(sermonEntity)
    }

    fun deleteAllSermons() = viewModelScope.launch {
        repository.deleteAllSermons()
    }
}

class SermonViewModelFactory(private val repository: SermonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SermonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SermonViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}