package com.example.note.model.usecase

import com.example.note.model.repository.UserRepository
import javax.inject.Inject

class DefaultForgotPasswordUseCaseImpl @Inject constructor(override val userRepository: UserRepository) :
    ForgotPasswordUseCase {
}