package com.example.note.model.usecase

import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import javax.inject.Singleton

@Singleton
interface EditNoteUseCase {
    val noteRepository: NoteRepository
    val userRepository: UserRepository
}