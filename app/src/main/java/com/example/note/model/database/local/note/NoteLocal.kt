package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.domain.supportquery.NoteWithTasks
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NoteLocal {
    fun findNotes(uid: Long): PagingSource<Int, Note>

    fun findNote(nid: Long): Single<NoteWithTasks>

    fun insertNotes(vararg notes: Note): Single<Int>

    fun insertTasks(vararg tasks: Task): Single<Int>

    fun updateNotes(vararg notes: Note): Single<Int>

    fun updateTasks(vararg tasks: Task): Single<Int>

    fun deleteNotes(vararg notes: Note): Single<Int>

    fun deleteTasks(vararg tasks: Task): Single<Int>
}