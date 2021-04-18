package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultUserInfoUseCaseImpl @Inject constructor(
    override val userRepository: UserRepository
) : UserInfoUseCase {

    override fun getUser(): Flowable<User> = userRepository.getUser().subscribeOn(Schedulers.io())
}