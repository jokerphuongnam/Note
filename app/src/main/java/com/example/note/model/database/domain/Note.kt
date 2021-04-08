package com.example.note.model.database.domain

import androidx.room.*
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey @ColumnInfo(name = "note_id") var nid: Long,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean,
    @ColumnInfo(name = "detail") var detail: String,
    @ColumnInfo(name = "tags") var tags: List<String>,
    @ColumnInfo(name = "images") var images: List<String>,
    @ColumnInfo(name = "sounds") var sounds: List<String>,
    @ColumnInfo(name = "notice_times") var noticeTimes: List<Date>,
    @ColumnInfo(name = "user_id") var userId: Long
){
    @Ignore lateinit var tasks: List<Task>
}