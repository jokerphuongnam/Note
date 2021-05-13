package com.example.note.model.usecase.impl

import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import com.example.note.model.usecase.EditNoteUseCase
import javax.inject.Inject

class DefaultEditNoteUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository
) : EditNoteUseCase