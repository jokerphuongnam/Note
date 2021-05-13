package com.example.note.model.database.local

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface LocalReference {
    val isDarkMode: Flowable<Boolean>
    fun changeTheme(boolean: Boolean): Single<Long>
}