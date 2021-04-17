package com.example.note.model.database.domain

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [
        Index(
            value = ["note_id"],
            unique = true
        )
    ]
)
data class Note(
    @PrimaryKey @ColumnInfo(name = "note_id") var nid: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "detail") var detail: String = "",
    @ColumnInfo(name = "tags") var tags: List<String> = emptyList(),
    @ColumnInfo(name = "images") var images: List<String>? = null,
    @ColumnInfo(name = "sounds") var sounds: List<String>? = null,
    @ColumnInfo(name = "notice_times") var noticeTimes: List<Long>? = null,
    @ColumnInfo(name = "user_id") var userId: Long? = null,
    @ColumnInfo(name = "modified_at") var modifiedAt: Long = 0,
    @ColumnInfo(name = "created_at") var createAt: Long = 0
) : Parcelable {
    @Ignore
    var tasks: MutableList<Task> = mutableListOf()
}