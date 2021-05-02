package com.example.note.model.usecase

import android.util.Log
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
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
        images: List<MultipartBody.Part>?,
        sounds: List<MultipartBody.Part>?,
        isUpdate: Boolean
    ): Single<Int> = userRepository.currentUser().flatMap { uid ->
        note.userId = uid
        val emptyTasks: MutableList<Task> = mutableListOf()
        note.tasks.forEach {task ->
            if(task.detail.trim().isEmpty()){
                Log.e("cccccccccccccccccc", task.toString())
                emptyTasks.add(task)
            }
        }
        emptyTasks.forEach { emptyTask->
            note.tasks.remove(emptyTask)
        }
        Log.e("cccccccccccccccccc", note.toString())
        if (isUpdate) {
            noteRepository.updateNote(note)
        } else {
            noteRepository.insertNote(note)
        }
    }.subscribeOn(Schedulers.io())

    override fun getNote(nid: Long): Single<Note> =
        noteRepository.getSingleNote(nid).subscribeOn(Schedulers.io())
}