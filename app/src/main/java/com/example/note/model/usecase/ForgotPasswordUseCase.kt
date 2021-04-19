package com.example.note.model.usecase

import com.example.note.model.repository.UserRepository
import javax.inject.Singleton

@Singleton
interface ForgotPasswordUseCase {
    val userRepository: UserRepository
}