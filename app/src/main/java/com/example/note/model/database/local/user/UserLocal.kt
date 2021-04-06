package com.example.note.model.database.local.user

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface UserLocal {
    fun findUsers(): Single<List<User>>

    fun insertUsers(vararg user: User): Int

    fun updateUsers(vararg user: User): Int

    fun deleteUsers(vararg user: User): Int
}