package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class DefaultUserInfoUseCaseImpl @Inject constructor(
    override val userRepository: UserRepository
) : UserInfoUseCase {

    override fun getUser(): Flowable<User> = userRepository.getUser().subscribeOn(Schedulers.io())

    override fun editProfile(user: User, avatar: MultipartBody.Part?): Single<User> =
        userRepository.editProfile(user, avatar).subscribeOn(Schedulers.io())
}