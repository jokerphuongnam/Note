package com.example.note.model.database.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.note.utils.TaskGsonAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import kotlinx.parcelize.IgnoredOnParcel
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
@JsonAdapter(TaskGsonAdapter::class)
data class Task(
    @ColumnInfo(name = "is_finish") var isFinish: Boolean = false,
    @ColumnInfo(name = "detail") var detail: String = ""
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    @Expose(serialize = false, deserialize = false)
    private var _taskId: Long = 0

    @IgnoredOnParcel
    @Expose(serialize = false, deserialize = false)
    @ColumnInfo(name = "note_id")
    private var _noteId: Long = 0

    var taskId: Long
        get() = _taskId
        set(value) {
            _taskId = value
        }

    var noteId: Long
        get() = _noteId
        set(value) {
            _noteId = value
        }
}