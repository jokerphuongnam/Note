package com.example.note.model.database.local.impl

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface UserLocal {
    fun findUsers(): Single<List<User>>

    fun findSingleUser(uid: Long): Flowable<User>

    fun insertUser(users: User): Single<Long>

    fun updateUsers(vararg users: User)

    fun deleteUsers(vararg users: User): Single<Int>

    fun deleteUser(uid: Long): Single<Int>
}