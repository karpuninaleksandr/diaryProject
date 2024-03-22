package com.example.diaryproject.addrecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import com.example.diaryproject.database.DiaryDatabaseDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRecordViewModel(
    private val recordId: Long = 0L,
    private val dao: DiaryDatabaseDAO) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    var tags = ""
    val tagsList = dao.getAllTagsList()

    private val _navigateToDiaryView = MutableLiveData<Boolean>()
    val navigateToDiaryView: LiveData<Boolean?>
        get() = _navigateToDiaryView

    private val _updateTagsList = MutableLiveData<String?>()
    val updateTagsList: LiveData<String?>
        get() = _updateTagsList

    fun doneNavigating() {
        _navigateToDiaryView.value = false
    }

    fun doneUpdating() {
        _updateTagsList.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onConfirm(lastChangeTime: Long, recordValue: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (recordValue.isNotBlank()) {
                    val record = dao.getRecordById(recordId) ?: return@withContext
                    record.lastChangeTimeMillis = lastChangeTime
                    record.tags = tags
                    record.recordValue = recordValue
                    dao.update(record)
                }
            }
            if (recordValue.isNotBlank()) _navigateToDiaryView.value = true
        }
    }

    fun onAddTag(position: Int) {
        uiScope.launch {
            val tag = dao.getTagById(tagsList[position].tagId)
            if (tag != null && !tags.contains(tag.tagName)) {
                tags = tags.plus(tag.tagName).plus(",")
                _updateTagsList.value = tags
            }
        }
    }

    fun onDeny() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (dao.getLastRecord()?.recordId == recordId) {
                    dao.deleteRecord(recordId)
                }
            }
            _navigateToDiaryView.value = true
        }
    }
}
