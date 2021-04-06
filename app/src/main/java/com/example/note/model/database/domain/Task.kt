package com.example.note.model.database.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @ColumnInfo(name = "is_finish") var isFinish: Boolean,
    @ColumnInfo(name = "detail") var detail: String,
    @ColumnInfo(name = "note_id") val noteId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private var _taskId: Long = 0

    var taskId: Long
        get() = _taskId
        set(value) {
            _taskId = value
        }
}