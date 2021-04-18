package com.example.note.model.usecase

import com.example.note.model.database.domain.User
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface MainUseCase {
    val noteRepository: NoteRepository
    val userRepository: UserRepository

    fun currentUser(): Single<Long>

    fun login(username: String, password: String, type: String): Single<User>
    fun getUser(): Flowable<User>
}