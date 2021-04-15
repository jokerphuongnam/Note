package com.example.note.model.usecase

import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Singleton

@Singleton
interface NoteInfoUseCase {
    val noteRepository: NoteRepository
    val userRepository: UserRepository

    fun deleteTask(
        vararg tasks: Task
    ): Single<Int>
}