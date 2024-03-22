package com.example.diaryproject.addtag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diaryproject.database.DiaryDatabaseDAO

class AddTagViewModelFactory(
    private val tagId: Long,
    private val dao: DiaryDatabaseDAO
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTagViewModel::class.java)) {
            return AddTagViewModel(tagId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}