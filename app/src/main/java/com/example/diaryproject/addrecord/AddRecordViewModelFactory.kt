package com.example.diaryproject.addrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diaryproject.database.DiaryDatabaseDAO

class AddRecordViewModelFactory(
    private val recordId: Long,
    private val dao: DiaryDatabaseDAO) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecordViewModel::class.java)) {
            return AddRecordViewModel(recordId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}