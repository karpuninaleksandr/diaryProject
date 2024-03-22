package com.example.diaryproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_records")
data class DiaryRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var recordId: Long = 0L,

    @ColumnInfo(name = "last_change_time_millis")
    var lastChangeTimeMillis: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "tags")
    var tags: String = "",

    @ColumnInfo(name = "record_value")
    var recordValue: String = ""
)