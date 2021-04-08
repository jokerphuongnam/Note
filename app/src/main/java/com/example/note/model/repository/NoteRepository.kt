package com.example.note.model.repository

import com.example.note.model.database.domain.Note
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.network.note.NoteNetwork
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface NoteRepository {
    val local: NoteLocal
    val network: NoteNetwork

    fun insertNote(
        uid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Response<Note>>>

    fun updateNote(
        uid: Long,
        nid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Response<Note>>>

    fun deleteNote(
        uid: Long,
        nid: Long
    ): Single<Response<Response<Note>>>

    fun getNotes(uid: Long, start: Int, amount: Int): Single<Response<MutableList<Note>>>
}