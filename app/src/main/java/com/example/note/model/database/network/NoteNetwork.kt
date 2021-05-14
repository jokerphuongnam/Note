package com.example.note.model.database.network

import android.graphics.Bitmap
import com.example.note.model.database.domain.Note
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface NoteNetwork {
    fun insertNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>
    ): Single<Response<Note>>

    fun updateNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>
    ): Single<Response<Note>>

    fun deleteNote(
        uid: Long,
        nid: Long
    ): Single<Response<Note>>

    fun fetchNotes(uid: Long, start: Long, amount: Long): Single<Response<MutableList<Note>>>

    fun fetchCount(uid: Long): Single<Response<Long>>
}