package com.example.note.model.usecase.impl

import com.example.note.model.database.domain.User
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import com.example.note.model.usecase.MainUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultMainUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository
) : MainUseCase {

    override fun currentUser(): Single<Long> =
        userRepository.currentUser().subscribeOn(Schedulers.io())

    override fun login(username: String, password: String, type: String): Single<User> =
        userRepository.login(username, password, type).subscribeOn(Schedulers.io())

    override fun getUser(): Flowable<User> = userRepository.getUser().subscribeOn(Schedulers.io())
}