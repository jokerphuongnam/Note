package com.example.note.model.repository

import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.network.note.NoteNetwork
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface NoteRepository {
    val local: NoteLocal
    val network: NoteNetwork

    fun insertNote(
        note: Note,
        images: List<MultipartBody.Part>? = null,
        sounds: List<MultipartBody.Part>? = null
    ): Single<Int>

    fun insertTask(
        vararg tasks: Task
    ):Single<Int>

    fun updateNote(
        nid: Long,
        note: Note,
        images: List<MultipartBody.Part>? = null,
        sounds: List<MultipartBody.Part>? = null
    ): Single<Int>

    fun updateTask(
        vararg tasks: Task
    ):Single<Int>

    fun deleteNote(
        uid: Long,
        nid: Long
    ): Single<Int>

    fun deleteTask(
        vararg tasks: Task
    ): Single<Int>

    fun clearTasksByNote(
        nid: Long
    ): Single<Int>

    fun getNotes(uid: Long): Flowable<PagingData<Note>>

    fun getSingleNote(): Single<Note>
}