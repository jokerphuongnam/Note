package com.example.note.model.usecase

import com.example.note.model.repository.UserRepository
import javax.inject.Inject

class DefaultLoginUseCaseImpl @Inject constructor(override val repository: UserRepository) : LoginUseCase