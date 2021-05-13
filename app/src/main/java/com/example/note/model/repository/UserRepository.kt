package com.example.note.model.repository

import android.net.Uri
import androidx.datastore.preferences.core.Preferences
import com.example.note.model.database.domain.User
import com.example.note.model.database.local.CurrentUser
import com.example.note.model.database.local.impl.UserLocal
import com.example.note.model.database.network.UserNetwork
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface UserRepository {
    val local: UserLocal
    val network: UserNetwork
    val currentUser: CurrentUser

    fun currentUser(): Single<Long>

    fun login(username: String, password: String, type: String): Single<User>

    fun logout(): Single<Preferences>

    fun deleteUser(uid: Long): Single<Int>

    fun editProfile(user: User, avatar: Uri?): Single<User>

    fun changePassword(
        user: User,
        oldPassword: String,
        newPassword: String
    ): Single<User>

    fun forgotPassword(username: String): Single<Int>

    fun register(
        user: User,
        password: String,
        type: String,
        avatar: Uri?
    ): Single<User>

    fun getUser(): Flowable<User>
}