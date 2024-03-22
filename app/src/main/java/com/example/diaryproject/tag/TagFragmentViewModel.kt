package com.example.diaryproject.tag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diaryproject.database.DiaryDatabaseDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TagFragmentViewModel(
    private val tagId: Long = 0L,
    private val dao: DiaryDatabaseDAO
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val tag = dao.getTagById(tagId)

    private val _navigateToTagListView = MutableLiveData<Boolean>()
    val navigateToTagListView: LiveData<Boolean?>
        get() = _navigateToTagListView

    fun doneNavigating() {
        _navigateToTagListView.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onGoToTagList() {
        _navigateToTagListView.value = true
    }

    fun onDeleteTag() {
        uiScope.launch {
            val tag  = dao.getTagById(tagId)
            val records = dao.getFilteredRecords("%".plus(tag?.tagName).plus("%"))
            for (record in records) {
                record.tags = record.tags.replace(tag?.tagName.plus(","), "")
                dao.update(record)
            }
            dao.deleteTag(tagId)
            _navigateToTagListView.value = true
        }
    }
}