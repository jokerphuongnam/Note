package com.example.note.model.usecase

import com.example.note.model.repository.UserRepository

interface ChangePasswordUseCase {
    val userRepository: UserRepository
}