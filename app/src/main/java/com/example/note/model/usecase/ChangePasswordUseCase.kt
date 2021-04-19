package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface ChangePasswordUseCase {
    val userRepository: UserRepository

    fun checkOldPassword(password: String): Single<User>

    fun changePassword(oldPassword: String,newPassword: String): Single<User>
}