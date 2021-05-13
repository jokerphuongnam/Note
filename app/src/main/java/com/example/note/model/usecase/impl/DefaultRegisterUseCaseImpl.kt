package com.example.note.model.usecase.impl

import android.graphics.Bitmap
import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import com.example.note.model.usecase.RegisterUseCase
import com.example.note.utils.RetrofitUtils.EMAIL_PASS
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultRegisterUseCaseImpl @Inject constructor(
    override val userRepository: UserRepository
) : RegisterUseCase {
    override fun register(user: User, password: String, avatar: Bitmap?): Single<User> =
        userRepository.register(user, password, EMAIL_PASS, avatar).subscribeOn(Schedulers.io())
}