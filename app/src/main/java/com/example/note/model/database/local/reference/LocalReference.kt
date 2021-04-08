package com.example.note.model.database.local.reference

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface LocalReference {
    val isDarkMode: Flowable<Boolean>
    fun changeTheme(boolean: Boolean): Single<Long>
}