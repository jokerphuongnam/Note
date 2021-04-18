package com.example.note.model.repository

import androidx.datastore.preferences.core.Preferences
import com.example.note.model.database.domain.User
import com.example.note.model.database.local.user.CurrentUser
import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.user.UserNetwork
import com.example.note.throwable.NotFoundException
import com.example.note.throwable.WrongException
import com.example.note.utils.RetrofitUtils.CONFLICT
import com.example.note.utils.RetrofitUtils.NOT_FOUND
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class DefaultUserRepositoryImpl @Inject constructor(
    override val local: UserLocal,
    override val network: UserNetwork,
    override val currentUser: CurrentUser
) : UserRepository {
    /**
     * get current user single flowable first time or first time is complete will return error
     * */
    override fun currentUser(): Single<Long> = currentUser.uid.firstOrError().map { uid ->
        uid ?: throw NotFoundException()
    }

    /**
     * when send login will receive 1 repose body if request code == 404 wrong username or password
     * add username for user and save for currentUser (data store) and room (save cache)
     * */
    override fun login(username: String, password: String, type: String): Single<User> =
        network.login(username, password, type).map {
            if (it.code().equals(NOT_FOUND)) {
                throw NotFoundException()
            } else {
                it.body()!!
            }
        }.flatMap { user ->
            user.username = username
            currentUser.changeCurrentUser(user.uid).flatMap {
                local.insertUser(user)
            }.map {
                user
            }
        }

    override fun logout(): Single<Preferences> = currentUser.signOut()

    override fun deleteUser(uid: Long): Single<Int> = local.deleteUser(uid)

    override fun getUser(): Flowable<User> = currentUser.uid.flatMap {
        local.findSingleUser(it!!)
    }

    override fun editUser(user: User, avatar: MultipartBody.Part?): Single<User> =
        network.editProfile(user, avatar).map {
            if (it.code().equals(CONFLICT)) {
                throw WrongException()
            } else {
                local.updateUsers(user)
                it.body()
            }
        }

    override fun changePassword(
        user: User,
        oldPassword: String,
        newPassword: String
    ): Single<User> = network.changePassword(user.username, oldPassword, newPassword).map {
        if (it.code().equals(NOT_FOUND)) {
            throw NotFoundException()
        } else {
            it.body()!!
        }
    }

    override fun forgotPassword(user: User): Single<User> =
        network.forgotPassword(user.username).map {
            if (it.code().equals(CONFLICT)) {
                throw NotFoundException()
            } else {
                it.body()!!
            }
        }


    override fun register(
        user: User,
        password: String,
        type: String,
        avatar: MultipartBody.Part?
    ): Single<User> = network.register(user, password, type, avatar).map {
        if (it.code().equals(CONFLICT)) {
            throw WrongException()
        } else {
            it.body()!!
        }
    }
}