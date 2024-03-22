package com.example.diaryproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryDatabaseDAO {

    @Insert
    fun insert(diaryRecord: DiaryRecord)

    @Insert
    fun insert(diaryTag: DiaryTag)

    @Query("DELETE FROM diary_records WHERE id = :key")
    fun deleteRecord(key: Long)

    @Query("DELETE FROM diary_tags WHERE id = :key")
    fun deleteTag(key: Long)

    @Update
    fun update(diaryRecord: DiaryRecord)

    @Update
    fun update(diaryTag: DiaryTag)

    @Query("SELECT * FROM diary_records ORDER BY last_change_time_millis DESC")
    fun getAllRecordsList(): List<DiaryRecord>

    @Query("SELECT * FROM diary_tags")
    fun getAllTags(): LiveData<List<DiaryTag>>

    @Query("SELECT * FROM diary_tags")
    fun getAllTagsList(): List<DiaryTag>

    @Query("SELECT * FROM diary_records ORDER BY id DESC LIMIT 1")
    fun getLastRecord(): DiaryRecord?

    @Query("SELECT * FROM diary_tags ORDER BY id DESC LIMIT 1")
    fun getLastTag(): DiaryTag?

    @Query("SELECT * FROM diary_records WHERE id = :key")
    fun getRecordById(key: Long): DiaryRecord?

    @Query("SELECT * FROM diary_tags WHERE id = :key")
    fun getTagById(key: Long): DiaryTag?

    @Query("SELECT * FROM diary_records WHERE tags LIKE :tagName ORDER BY last_change_time_millis DESC")
    fun getFilteredRecords(tagName: String): List<DiaryRecord>
}