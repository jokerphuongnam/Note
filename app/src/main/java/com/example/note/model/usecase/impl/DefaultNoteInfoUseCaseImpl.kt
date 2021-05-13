package com.example.note.model.usecase.impl

import android.graphics.Bitmap
import android.net.Uri
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import com.example.note.model.usecase.NoteInfoUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultNoteInfoUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository
) : NoteInfoUseCase {
    override fun deleteTask(vararg tasks: Task): Single<Int> =
        noteRepository.deleteTask(*tasks).subscribeOn(Schedulers.io())

    override fun deleteNote(note: Note): Single<Long> =
        userRepository.currentUser().flatMap { userId ->
            note.userId = userId
            noteRepository.deleteNote(note)
        }.subscribeOn(Schedulers.io())

    override fun saveNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>,
        isUpdate: Boolean
    ): Single<Int> = userRepository.currentUser().flatMap { uid ->
        note.userId = uid
        val emptyTasks: MutableList<Task> = mutableListOf()
        note.tasks.forEach {task ->
            if(task.detail.trim().isEmpty()){
                emptyTasks.add(task)
            }
        }
        emptyTasks.forEach { emptyTask->
            note.tasks.remove(emptyTask)
        }
        if (isUpdate) {
            noteRepository.updateNote(
                note,
                images,
                sounds
            )
        } else {
            noteRepository.insertNote(
                note,
                images,
                sounds
            )
        }
    }.subscribeOn(Schedulers.io())

    override fun getNote(nid: Long): Single<Note> =
        noteRepository.getSingleNote(nid).subscribeOn(Schedulers.io())
}