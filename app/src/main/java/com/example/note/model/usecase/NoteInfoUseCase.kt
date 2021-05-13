package com.example.note.model.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Singleton

@Singleton
interface NoteInfoUseCase {
    val noteRepository: NoteRepository
    val userRepository: UserRepository

    fun deleteTask(
        vararg tasks: Task
    ): Single<Int>

    fun deleteNote(note: Note): Single<Long>

    fun saveNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>,
        isUpdate: Boolean = false
    ): Single<Int>

    fun getNote(nid: Long): Single<Note>
}