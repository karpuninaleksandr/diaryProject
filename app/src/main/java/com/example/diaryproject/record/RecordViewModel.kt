package com.example.diaryproject.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diaryproject.database.DiaryDatabaseDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RecordViewModel(
    private val recordId: Long = 0L,
    private val dao: DiaryDatabaseDAO) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    val record = dao.getRecordById(recordId)
    var tags = record?.tags
    var tagsList = dao.getAllTagsList()

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

    fun onGoToDiary(recordValue: String) {
        uiScope.launch {
            val record = dao.getRecordById(recordId)
            if (record != null && recordValue.isNotBlank() && (recordValue != record.recordValue || record.tags != tags)) {
                record.recordValue = recordValue
                record.tags = tags.toString()
                record.lastChangeTimeMillis = System.currentTimeMillis()
                dao.update(record)
                _navigateToDiaryView.value = true
            }
        }
    }

    fun onDeleteRecord() {
        uiScope.launch {
            dao.deleteRecord(recordId)
            _navigateToDiaryView.value = true
        }
    }

    fun onAddTag(position: Int) {
        uiScope.launch {
            val tag = tagsList[position]
            if (tags?.contains(tag.tagName) == false) {
                tags = tags.plus(tag.tagName).plus(",")
            }
            _updateTagsList.value = tags
        }
    }

    fun onDeleteTag(position: Int) {
        uiScope.launch {
            val tag = tagsList[position]
            if (tags?.contains(tag.tagName) == true) {
                tags = tags!!.replace(tag.tagName.plus(","), "")
            }
            _updateTagsList.value = tags
        }
    }
}