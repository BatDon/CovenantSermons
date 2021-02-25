package com.example.covenantsermons.repository

import androidx.annotation.WorkerThread
import com.example.covenantsermons.dao.SermonDao
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.modelClass.SermonEntity
import kotlinx.coroutines.flow.Flow

class SermonRepository(private val sermonDao: SermonDao) {

    val allSermons: Flow<List<SermonEntity>> = sermonDao.getDateOrderedSermons()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(sermonEntity: SermonEntity) {
        sermonDao.insert(sermonEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(sermonEntity: SermonEntity){
        sermonDao.delete(sermonEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllSermons(){
        sermonDao.deleteAllSermons()
    }


}