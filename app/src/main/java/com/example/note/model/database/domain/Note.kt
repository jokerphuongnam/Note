package com.example.note.model.database.domain

import androidx.room.*
import java.util.Date

@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Note(
    @PrimaryKey @ColumnInfo(name = "note_id") var nid: Long,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean,
    @ColumnInfo(name = "detail") var detail: String,
    @ColumnInfo(name = "tags") var tags: List<String>,
    @ColumnInfo(name = "images") var images: List<String>? = null,
    @ColumnInfo(name = "sounds") var sounds: List<String>? = null,
    @ColumnInfo(name = "notice_times") var noticeTimes: List<Date>? = null,
    @ColumnInfo(name = "user_id") var userId: Long? = null,
    @ColumnInfo(name = "modified_at") var modifiedAt: Long
) {
    @Ignore
    lateinit var tasks: List<Task>
}