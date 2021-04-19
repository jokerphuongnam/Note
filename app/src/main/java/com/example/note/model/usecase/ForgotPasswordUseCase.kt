package com.example.note.model.usecase

import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface ForgotPasswordUseCase {
    val userRepository: UserRepository

    fun forgotPassword(username: String): Single<Int>
}