package com.example.note.model.usecase

import androidx.datastore.preferences.core.Preferences
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface SettingUseCase {
    val userRepository: UserRepository

    fun logout(): Single<Preferences>
}