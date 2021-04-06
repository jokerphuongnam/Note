package com.example.note.model.database.local.note

import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomNoteImpl @Inject constructor(private val dao: NoteDao): NoteLocal {

    override fun findNotes(uid: Long): Single<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun insertNotes(vararg notes: Note): Int {
        TODO("Not yet implemented")
    }

    override fun insertTasks(vararg tasks: Task): Int {
        TODO("Not yet implemented")
    }

    override fun updateNotes(vararg notes: Note): Int {
        TODO("Not yet implemented")
    }

    override fun updateTasks(vararg tasks: Task): Int {
        TODO("Not yet implemented")
    }

    override fun deleteNotes(vararg notes: Note): Int {
        TODO("Not yet implemented")
    }

    override fun deleteTask(vararg task: Task): Int {
        TODO("Not yet implemented")
    }

    interface NoteDao {

    }
}