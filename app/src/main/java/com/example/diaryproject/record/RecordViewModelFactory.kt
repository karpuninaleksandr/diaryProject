package com.example.diaryproject.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diaryproject.database.DiaryDatabaseDAO

class RecordViewModelFactory(
    private val recordId: Long,
    private val dao: DiaryDatabaseDAO
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordViewModel::class.java)) {
            return RecordViewModel(recordId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}