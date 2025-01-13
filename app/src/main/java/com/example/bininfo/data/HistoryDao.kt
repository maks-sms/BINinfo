package com.example.bininfo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyItem: HistoryItem)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<HistoryItem>>
}
