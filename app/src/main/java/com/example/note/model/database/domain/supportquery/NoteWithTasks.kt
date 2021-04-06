 package com.example.note.model.database.domain.supportquery

import androidx.room.Embedded
import androidx.room.Relation
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task

data class NoteWithTasks(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "",
        entityColumn = ""
    )
    val tasks: List<Task>
)