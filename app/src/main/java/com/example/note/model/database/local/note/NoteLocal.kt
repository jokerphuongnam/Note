package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.domain.supportquery.NoteWithTasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NoteLocal {
    fun findNotes(uid: Long): PagingSource<Int, Note>

    fun findNote(nid: Long): Single<Note>

    fun insertNotes(vararg notes: Note): Completable

    fun insertNotesWithTimestamp(vararg notes: Note): Completable

    fun insertTasks(vararg tasks: Task)

    fun updateNotes(vararg notes: Note): Single<Int>

    fun updateTasks(vararg tasks: Task): Single<Int>

    fun deleteNotes(vararg notes: Note): Single<Int>

    fun deleteNotes(uid: Long): Single<Int>

    fun deleteTasks(vararg tasks: Task): Single<Int>
}