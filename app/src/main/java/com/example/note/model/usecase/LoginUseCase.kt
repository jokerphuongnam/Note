package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface LoginUseCase {
    val repository: UserRepository

    fun loginEmailPass(username: String, password: String): Single<User>
}