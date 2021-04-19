package com.example.note.model.usecase

import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultForgotPasswordUseCaseImpl @Inject constructor(override val userRepository: UserRepository) :
    ForgotPasswordUseCase {
    override fun forgotPassword(username: String): Single<Int> =
        userRepository.forgotPassword(username).subscribeOn(Schedulers.io())
}