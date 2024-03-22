package com.example.diaryproject.taglistviewment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diaryproject.database.DiaryDatabaseDAO

class TagListViewModelFactory(private val dao: DiaryDatabaseDAO,
private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagListViewModel::class.java)) {
            return TagListViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}