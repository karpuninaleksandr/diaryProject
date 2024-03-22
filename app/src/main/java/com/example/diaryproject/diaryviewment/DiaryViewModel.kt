package com.example.diaryproject.diaryviewment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.diaryproject.database.DiaryDatabaseDAO
import com.example.diaryproject.database.DiaryRecord
import com.example.diaryproject.database.DiaryTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DiaryViewModel(
    private val dao: DiaryDatabaseDAO,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    var tags: List<DiaryTag> = dao.getAllTagsList().filter { dao.getAllRecordsList().map {
        rec -> rec.tags }.any { rec -> rec.contains(it.tagName) } }

    private val _navigateToAddRecord = MutableLiveData<DiaryRecord?>()
    val navigateToAddRecord: LiveData<DiaryRecord?>
        get() = _navigateToAddRecord

    private val _navigateToRecord = MutableLiveData<DiaryRecord?>()
    val navigateToRecord: LiveData<DiaryRecord?>
        get() = _navigateToRecord

    private val _navigateToTagListView = MutableLiveData<Boolean>()
    val navigateToTagListView: LiveData<Boolean?>
        get() = _navigateToTagListView

    private val _updateRecordList = MutableLiveData<List<DiaryRecord?>?>()
    val updateRecordList: LiveData<List<DiaryRecord?>?>
        get() = _updateRecordList

    fun onAddRecord() {
        uiScope.launch {
            val newRecord = DiaryRecord()
            dao.insert(newRecord)
            _navigateToAddRecord.value = dao.getLastRecord()
        }
    }

    fun onGoToTagListView() {
        uiScope.launch {
            _navigateToTagListView.value = true
        }
    }

    fun onRecordClick(recordId: Long) {
        uiScope.launch {
            _navigateToRecord.value = dao.getRecordById(recordId)
        }
    }

    fun onFilter(position: Int) {
        uiScope.launch {
            if (position == 0) {
                _updateRecordList.value = dao.getFilteredRecords("%%")
            } else {
                _updateRecordList.value =
                    tags[position - 1].let { it ->
                        dao.getTagById(it.tagId)?.let {
                            dao.getFilteredRecords("%".plus(it.tagName).plus("%"))
                        }.orEmpty()
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneUpdating() {
        _updateRecordList.value = null
    }

    fun doneNavigating() {
        _navigateToAddRecord.value = null
        _navigateToTagListView.value = false
        _navigateToRecord.value = null
    }
}