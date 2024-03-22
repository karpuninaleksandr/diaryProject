package com.example.diaryproject.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diaryproject.database.DiaryDatabaseDAO

class TagFragmentViewModelFactory(
    private val recordId: Long,
    private val dao: DiaryDatabaseDAO
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagFragmentViewModel::class.java)) {
            return TagFragmentViewModel(recordId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}