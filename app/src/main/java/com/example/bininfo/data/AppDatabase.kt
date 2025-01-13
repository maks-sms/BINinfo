package com.example.bininfo.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Request::class, HistoryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
    abstract fun getHistoryDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDb(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bin.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}
