package com.example.note.model.database.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = Note::class,
        parentColumns = arrayOf("note_id"),
        childColumns = arrayOf("note_id"),
        onDelete = CASCADE,
        onUpdate = CASCADE
    )]
)
data class Task(
    @ColumnInfo(name = "is_finish") var isFinish: Boolean,
    @ColumnInfo(name = "detail") var detail: String,
    @ColumnInfo(name = "note_id") val noteId: Long
) : Parcelable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private var _taskId: Long = 0

    var taskId: Long
        get() = _taskId
        set(value) {
            _taskId = value
        }
}