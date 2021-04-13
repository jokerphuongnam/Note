package com.example.note.model.usecase

import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultNotesUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository
) : NotesUseCase {

    override fun getNotes(): Flowable<PagingData<Note>> =
        noteRepository.getNotes().subscribeOn(Schedulers.io())
}