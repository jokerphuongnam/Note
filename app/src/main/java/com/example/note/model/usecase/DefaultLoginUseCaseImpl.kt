package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import com.example.note.utils.RetrofitConstrain.EMAIL_PASS
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultLoginUseCaseImpl @Inject constructor(
    override val repository: UserRepository
) : LoginUseCase {
    override fun loginEmailPass(username: String, password: String): Single<User> =
        repository.login(username, password, EMAIL_PASS).subscribeOn(Schedulers.io())
}