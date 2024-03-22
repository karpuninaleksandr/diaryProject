package com.example.diaryproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_tags")
data class DiaryTag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var tagId: Long = 0L,

    @ColumnInfo(name = "tag_name")
    var tagName: String = "",
)
