package com.example.note.model.usecase

import android.content.Context
import android.net.Uri
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.repository.NoteRepository
import com.example.note.model.repository.UserRepository
import com.example.note.ui.noteinfo.toMultipartBodies
import com.example.note.utils.RetrofitUtils.IMAGES
import com.example.note.utils.RetrofitUtils.SOUNDS
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DefaultNoteInfoUseCaseImpl @Inject constructor(
    override val noteRepository: NoteRepository,
    override val userRepository: UserRepository,
    @ApplicationContext private val context: Context
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
        images: List<Uri>,
        sounds: List<Uri>,
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
                images.toMultipartBodies(IMAGES, context),
                sounds.toMultipartBodies(SOUNDS, context)
            )
        } else {
            noteRepository.insertNote(
                note,
                images.toMultipartBodies(IMAGES, context),
                sounds.toMultipartBodies(SOUNDS, context)
            )
        }
    }.subscribeOn(Schedulers.io())

    override fun getNote(nid: Long): Single<Note> =
        noteRepository.getSingleNote(nid).subscribeOn(Schedulers.io())
}