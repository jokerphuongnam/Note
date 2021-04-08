package com.example.note.model.database.local.user

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface UserLocal {
    fun findUsers(): Single<List<User>>

    fun insertUsers(vararg users: User): Completable

    fun updateUsers(vararg users: User): Single<Int>

    fun deleteUsers(vararg users: User): Single<Int>
}