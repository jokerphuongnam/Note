package com.example.note.model.repository

import androidx.datastore.preferences.core.Preferences
import com.example.note.model.database.domain.User
import com.example.note.model.database.local.user.CurrentUser
import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.user.UserNetwork
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Singleton

@Singleton
interface UserRepository {
    val local: UserLocal
    val network: UserNetwork
    val currentUser: CurrentUser

    fun currentUser(): Flowable<Long>

    fun login(username: String, password: String, type: String): Single<User>

    fun logout(): Single<Preferences>

    fun deleteUser(uid: Long): Single<Int>

    fun editUser(user: User, avatar: MultipartBody.Part? = null): Single<User>

    fun changePassword(
        user: User,
        oldPassword: String,
        newPassword: String
    ): Single<User>

    fun forgotPassword(user: User): Single<User>

    fun register(
        user: User,
        password: String,
        type: String,
        avatar: MultipartBody.Part? = null
    ): Single<User>
}