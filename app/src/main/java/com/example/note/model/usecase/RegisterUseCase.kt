package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

interface RegisterUseCase {
    val userRepository: UserRepository

    fun register(
        user: User,
        password: String,
        avatar: MultipartBody.Part? = null
    ): Single<User>
}