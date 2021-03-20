package com.example.covenantsermons.viewmodel

import androidx.lifecycle.*
import com.example.covenantsermons.modelClass.SermonEntity
import com.example.covenantsermons.repository.SermonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SermonViewModel(private val repository: SermonRepository) : ViewModel() {

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

    fun count(date: String):Int= runBlocking{

         val count=async{repository.count(date)}
        return@runBlocking count.await()
    }
//    fun count(date: String):Int{
//        var sermonEntityCount=0
//        viewModelScope.launch {
//            sermonEntityCount=repository.count(date)
//        }
//        return sermonEntityCount
//    }
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