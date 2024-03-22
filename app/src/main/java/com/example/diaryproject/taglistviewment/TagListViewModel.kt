package com.example.diaryproject.taglistviewment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.diaryproject.database.DiaryDatabaseDAO
import com.example.diaryproject.database.DiaryTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TagListViewModel(
    private val dao: DiaryDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val tags = dao.getAllTags()

    private val _navigateToAddTag = MutableLiveData<DiaryTag?>()

    val navigateToAddTag: LiveData<DiaryTag?>
        get() = _navigateToAddTag

    private val _navigateToTag = MutableLiveData<DiaryTag?>()

    val navigateToTag: LiveData<DiaryTag?>
        get() = _navigateToTag

    private val _navigateToDiaryView = MutableLiveData<Boolean>()

    val navigateToDiaryView: LiveData<Boolean?>
        get() = _navigateToDiaryView

    fun onAddTag() {
        uiScope.launch {
            val newTag = DiaryTag()
            dao.insert(newTag)
            _navigateToAddTag.value = dao.getLastTag()
        }
    }

    fun onGoToDiary() {
        uiScope.launch {
            _navigateToDiaryView.value = true
        }
    }

    fun onTagClick(tagId: Long) {
        uiScope.launch {
            _navigateToTag.value = dao.getTagById(tagId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating() {
        _navigateToAddTag.value = null
        _navigateToDiaryView.value = false
        _navigateToTag.value = null
    }
}