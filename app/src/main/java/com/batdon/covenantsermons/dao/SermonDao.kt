package com.batdon.covenantsermons.dao

import androidx.room.*
import com.batdon.covenantsermons.modelClass.SermonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SermonDao {

    @Query("SELECT * FROM sermon_table ORDER BY date ASC")
    fun getDateOrderedSermons(): Flow<List<SermonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sermonEntity: SermonEntity)

    @Delete
    suspend fun delete(sermonEntity: SermonEntity)

    @Query("DELETE FROM sermon_table")
    suspend fun deleteAllSermons()

    //check if SermonEntity exists
    @Query("SELECT COUNT() FROM sermon_table WHERE date = :date")
    suspend fun count(date: String): Int

}