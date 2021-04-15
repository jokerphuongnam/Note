package com.example.note.model.usecase

import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultNoteInfoUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository
) : NoteInfoUseCase {
    override fun deleteTask(vararg tasks: Task): Single<Int> =
        noteRepository.deleteTask(*tasks).observeOn(Schedulers.io())

    override fun saveNote(note: Note): Single<Int>  = userRepository.currentUser().flatMap { uid->
        noteRepository.insertNote(note.apply {
            userId = uid
        })
    }.observeOn(Schedulers.io())
}