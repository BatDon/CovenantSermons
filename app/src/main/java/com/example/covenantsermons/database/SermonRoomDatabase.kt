package com.example.covenantsermons.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.covenantsermons.dao.SermonDao
import com.example.covenantsermons.modelClass.Sermon
import kotlinx.coroutines.CoroutineScope


@Database(entities = [Sermon::class], version = 1)
abstract class SermonRoomDatabase : RoomDatabase() {

    abstract fun sermonDao(): SermonDao

    companion object {
        @Volatile
        private var INSTANCE: SermonRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): SermonRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SermonRoomDatabase::class.java,
                    "sermon_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
//                    .addCallback(SermonDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

//        private class SermonDatabaseCallback(
//            private val scope: CoroutineScope
//        ) : RoomDatabase.Callback() {
//            /**
//             * Override the onCreate method to populate the database.
//             */
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                // If you want to keep the data through app restarts,
//                // comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.sermonDao())
//                    }
//                }
//            }
//        }

    }
}