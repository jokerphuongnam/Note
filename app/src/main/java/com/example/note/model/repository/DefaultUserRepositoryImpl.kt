package com.example.note.model.repository

import com.example.note.model.database.domain.User
import com.example.note.model.database.local.user.CurrentUser
import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.user.UserNetwork
import com.example.note.throwable.NotFoundException
import com.example.note.throwable.WrongException
import com.example.note.utils.RetrofitConstrain.CONFLICT
import com.example.note.utils.RetrofitConstrain.NOT_FOUND
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import javax.inject.Inject

class DefaultUserRepositoryImpl @Inject constructor(
    override val local: UserLocal,
    override val network: UserNetwork,
    override val currentUser: CurrentUser
) : UserRepository {
    override fun currentUser(): Single<Long> = currentUser.uid.map { uid->
        uid ?: throw NotFoundException()
    }

    override fun login(username: String, password: String, type: String): Single<User> =
        network.login(username, password, type).map {
            if (it.code().equals(NOT_FOUND)) {
                throw NotFoundException()
            } else {
                it.body()
            }
        }.map { user ->
            if (user != null) {
                currentUser.changeCurrentUser(user.uid)
                local.insertUsers(user)
            }
            user
        }

    override fun logout(): Completable = currentUser.signOut()

    override fun deleteUser(uid: Long): Single<Int> = local.deleteUser(uid)

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