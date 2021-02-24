package com.example.covenantsermons.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.covenantsermons.modelClass.Sermon
import kotlinx.coroutines.flow.Flow

interface SermonDao {

    @Query("SELECT * FROM sermon_table ORDER BY date ASC")
    fun getDateOrderedSermons(): Flow<List<Sermon>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sermon: Sermon)

    @Delete
    suspend fun delete(sermon: Sermon)

    @Query("DELETE FROM sermon_table")
    suspend fun deleteAllSermons()

}