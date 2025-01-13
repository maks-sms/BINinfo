package com.example.bininfo.data

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@androidx.room.Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItem(item: Request)

    @Query("SELECT * FROM requests WHERE name LIKE :name")
    fun getItem(name: String): LiveData<Request>

    @Query("SELECT name FROM requests")
    suspend fun requestsList(): Array<String>
}