package com.example.note.model.database.network.note

import com.example.note.model.database.domain.Note
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import java.util.*
import javax.inject.Singleton

@Singleton
interface NoteNetwork {
    fun insertNote(
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Note>>

    fun updateNote(
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Note>>

    fun deleteNote(
        uid: Long,
        nid: Long
    ): Single<Response<Note>>

    fun fetchNotes(uid: Long, start: Long, amount: Long): Single<Response<MutableList<Note>>>

    fun fetchCount(uid: Long): Single<Response<Long>>
}