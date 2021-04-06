package com.example.note.model.database.local.note

import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NoteLocal {
    fun findNotes(uid: Long): Single<List<Note>>

    fun insertNotes(vararg notes: Note): Int

    fun insertTasks(vararg tasks: Task): Int

    fun updateNotes(vararg notes: Note): Int

    fun updateTasks(vararg tasks: Task): Int

    fun deleteNotes(vararg notes: Note): Int

    fun deleteTask(vararg task: Task): Int
}