package com.example.note.model.database.local.user

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface CurrentUser {
    val uid: Single<Long?>
    fun changeCurrentUser(uid: Long): Completable
    fun signOut(): Completable
}