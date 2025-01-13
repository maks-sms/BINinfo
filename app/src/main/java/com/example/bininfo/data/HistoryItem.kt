package com.example.bininfo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "bin")
    val bin: String,
    @ColumnInfo(name = "brand")
    val brand: String,
    @ColumnInfo(name = "country")
    val country: String
)
