package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import com.example.note.utils.RetrofitUtils
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultChangePasswordUseCaseImpl @Inject constructor(override val userRepository: UserRepository) :
    ChangePasswordUseCase {
    override fun checkOldPassword(password: String): Single<User> =
        userRepository.getUser().firstOrError().flatMap { user ->
            userRepository.login(user.username, password, RetrofitUtils.EMAIL_PASS)
        }.subscribeOn(Schedulers.io())

    override fun changePassword(oldPassword: String, newPassword: String): Single<User> =
        userRepository.getUser().firstOrError().flatMap { user ->
            userRepository.changePassword(user, oldPassword, newPassword)
        }.subscribeOn(Schedulers.io())
}