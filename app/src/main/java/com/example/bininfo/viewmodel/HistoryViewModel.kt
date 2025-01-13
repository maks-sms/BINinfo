package com.example.bininfo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bininfo.data.AppDatabase
import com.example.bininfo.data.HistoryDao
import com.example.bininfo.data.HistoryItem
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val historyDao: HistoryDao = AppDatabase.getDb(application).getHistoryDao()

    val historyItems: LiveData<List<HistoryItem>> = historyDao.getAllHistory()

    fun addHistoryItem(historyItem: HistoryItem) {
        viewModelScope.launch {
            historyDao.insert(historyItem)
        }
    }
}
