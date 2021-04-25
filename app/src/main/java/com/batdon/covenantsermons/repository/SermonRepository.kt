package com.batdon.covenantsermons.repository

import androidx.annotation.WorkerThread
import com.batdon.covenantsermons.dao.SermonDao
import com.batdon.covenantsermons.modelClass.SermonEntity
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun count(date: String): Int {
        return sermonDao.count(date)
    }


}