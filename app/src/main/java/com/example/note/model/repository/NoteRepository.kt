package com.example.note.model.repository

import android.graphics.Bitmap
import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.local.NoteLocal
import com.example.note.model.database.network.NoteNetwork
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NoteRepository {
    val local: NoteLocal
    val network: NoteNetwork

    fun insertNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>
    ): Single<Int>

    fun insertTasks(
        vararg tasks: Task
    ): Single<Int>

    fun updateNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>
    ): Single<Int>

    fun updateTask(
        vararg tasks: Task
    ): Single<Int>

    fun deleteNote(note: Note): Single<Long>

    fun deleteTask(
        vararg tasks: Task
    ): Single<Int>

    fun clearTasksByNote(
        nid: Long
    ): Single<Int>

    fun getNotes(uid: Long): Flowable<PagingData<Note>>

    fun getSingleNote(uid: Long): Single<Note>

    fun clearNotes(uid: Long): Completable
}