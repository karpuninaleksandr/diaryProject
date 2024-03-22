package com.example.diaryproject.addtag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diaryproject.database.DiaryDatabaseDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTagViewModel(
    private val tagId: Long = 0L,
    private val dao: DiaryDatabaseDAO
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

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

    fun onConfirmAddRecord(tagName: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (tagName.isNotBlank()) {
                    val tag = dao.getTagById(tagId) ?: return@withContext
                    tag.tagName = tagName
                    dao.update(tag)
                }
            }
            if (tagName.isNotBlank()) _navigateToTagListView.value = true
        }
    }

    fun onDenyAddRecord() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteTag(tagId)
            }
            _navigateToTagListView.value = true
        }
    }
}
