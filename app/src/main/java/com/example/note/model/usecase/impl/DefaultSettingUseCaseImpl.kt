package com.example.note.model.usecase.impl

import androidx.datastore.preferences.core.Preferences
import com.example.note.model.repository.UserRepository
import com.example.note.model.usecase.SettingUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultSettingUseCaseImpl @Inject constructor(override val userRepository: UserRepository) :
    SettingUseCase {
    override fun logout() : Single<Preferences> = userRepository.logout().subscribeOn(Schedulers.io())
}