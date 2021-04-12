package com.example.note.model.database.local.user

import androidx.datastore.preferences.core.Preferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface CurrentUser {
    val uid: Flowable<Long?>
    fun changeCurrentUser(uid: Long): Single<Preferences>
    fun signOut(): Single<Preferences>
}