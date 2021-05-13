package com.example.note.model.usecase

import android.graphics.Bitmap
import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single

interface RegisterUseCase {
    val userRepository: UserRepository

    fun register(
        user: User,
        password: String,
        avatar: Bitmap? = null
    ): Single<User>
}