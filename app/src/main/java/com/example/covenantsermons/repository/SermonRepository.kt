package com.example.covenantsermons.repository

import androidx.annotation.WorkerThread
import com.example.covenantsermons.dao.SermonDao
import com.example.covenantsermons.modelClass.Sermon
import kotlinx.coroutines.flow.Flow

class SermonRepository(private val sermonDao: SermonDao) {

    val allSermons: Flow<List<Sermon>> = sermonDao.getDateOrderedSermons()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(sermon: Sermon) {
        sermonDao.insert(sermon)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(sermon: Sermon){
        sermonDao.delete(sermon)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllSermons(){
        sermonDao.deleteAllSermons()
    }


}