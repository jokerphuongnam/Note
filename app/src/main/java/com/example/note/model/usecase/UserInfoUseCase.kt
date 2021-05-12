package com.example.note.model.usecase

import android.net.Uri
import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

interface UserInfoUseCase {
    val userRepository: UserRepository

    fun getUser(): Flowable<User>

    fun editProfile(user: User, avatar: Uri): Single<User>
    fun editProfile(user: User): Single<User>
}