package com.example.note.model.usecase.impl

import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import com.example.note.model.usecase.NotesUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultNotesUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository
) : NotesUseCase {


    override fun getNotes(): Flowable<PagingData<Note>> =
        userRepository.currentUser().toFlowable().flatMap { uid ->
            noteRepository.getNotes(uid)
        }

    override fun deleteTask(vararg tasks: Task): Single<Int> =
        noteRepository.deleteTask(*tasks).observeOn(Schedulers.io())

    /**
     * get id note from user repository
     * set uid for note
     * delete note
     * */
    override fun deleteNote(note: Note): Single<Long> = userRepository.currentUser().flatMap { userId ->
        note.userId = userId
        noteRepository.deleteNote(note)
    }.subscribeOn(Schedulers.io())
}