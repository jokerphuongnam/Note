package com.example.note.model.database.domain.supportquery

import androidx.room.Embedded
import androidx.room.Relation
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.User

data class UserWithNotes(
    @Embedded val user: User,
    @Relation(
        parentColumn = "note_id",
        entityColumn = "note_id"
    )
    val notes: List<Note>
)