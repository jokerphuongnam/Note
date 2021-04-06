package com.example.note.model.database.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey @ColumnInfo(name = "note_id")val nid: Long,
    @ColumnInfo(name = "title")var title: String,
    @ColumnInfo(name = "is_favorite")var isFavorite: Boolean,
    @ColumnInfo(name = "detail")var detail: String,
    @ColumnInfo(name = "tags") var tags: MutableList<String>,
    @Ignore var tasks: MutableList<Task>,
    @ColumnInfo(name = "images")var images: MutableList<String>,
    @ColumnInfo(name = "sounds")var sounds: MutableList<String>,
    @ColumnInfo(name = "notice_times")var noticeTimes: MutableList<Date>,
    @ColumnInfo(name = "user_id") var userId: Long
)