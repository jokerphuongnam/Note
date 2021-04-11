package com.example.note.model.database.network.user

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface UserNetwork {
    fun login(username: String, password: String, type: String): Single<Response<User>>

    fun register(
        user: User,
        password: String,
        type: String,
        avatar: MultipartBody.Part? = null
    ): Single<Response<User>>

    fun editProfile(user: User, avatar: MultipartBody.Part? = null): Single<Response<User>>

    fun changePassword(
        username: String,
        oldPassword: String,
        newPassword: String
    ): Single<Response<User>>

    fun forgotPassword(username: String): Single<Response<User>>
}