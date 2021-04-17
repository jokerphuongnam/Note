package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NoteLocal {
    fun findNotesPaging(uid: Long): PagingSource<Int, Note>

    fun findNotes(uid: Long): Flowable<MutableList<Note>>

    fun findLastUpdateSingle(uid: Long): Single<Note>

    fun findSingleNote(nid: Long): Single<Note>

    fun findTasksByUid(nid: Long): Single<List<Task>>

    fun insertNotesWithTime(notes: List<Note>)

    fun insertNotes(notes: List<Note>)

    fun insertTasks(vararg tasks: Task)

    fun updateNotes(vararg notes: Note): Single<Int>

    fun updateTasks(vararg tasks: Task): Single<Int>

    fun deleteNotes(vararg notes: Note): Single<Int>

    fun deleteNotes(uid: Long): Single<Int>

    fun deleteTasks(vararg tasks: Task): Single<Int>

    fun clearTasksByNote(uid: Long): Single<Int>
}