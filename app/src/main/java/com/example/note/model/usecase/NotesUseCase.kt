package com.example.note.model.usecase

import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NotesUseCase {
    val noteRepository: NoteRepository
    val userRepository: UserRepository

    fun getNotes(): Flowable<PagingData<Note>>
    fun deleteTask(vararg tasks: Task): Single<Int>
    fun deleteNote(note: Note): Single<Long>
}