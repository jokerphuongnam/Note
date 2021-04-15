package com.example.note.model.database.domain.supportquery

import androidx.room.Embedded
import androidx.room.Relation
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task

data class NoteWithTasks(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "note_id",
        entityColumn = "note_id"
    )
    val tasks: MutableList<Task>
) {
    /**
     * find note will take a noteWithTask later convert to note
     * - get note by noteWithTask
     * - get tasks and assign for tasks in note
     * */
    fun toNote(): Note = note.apply {
        tasks = this@NoteWithTasks.tasks
    }
}