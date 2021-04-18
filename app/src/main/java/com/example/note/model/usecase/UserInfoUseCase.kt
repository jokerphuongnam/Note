package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable

interface UserInfoUseCase {
    val userRepository: UserRepository

    fun getUser(): Flowable<User>
}