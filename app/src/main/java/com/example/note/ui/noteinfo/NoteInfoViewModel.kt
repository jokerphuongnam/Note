package com.example.note.ui.noteinfo

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.usecase.NoteInfoUseCase
import com.example.note.throwable.NoConnectivityException
import com.example.note.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class NoteInfoViewModel @Inject constructor(private val useCase: NoteInfoUseCase) :
    BaseViewModel() {

    private val singleObserver: SingleObserver<Int> by lazy {
        object : SingleObserver<Int> {
            private var saveNoteDisable: Disposable? = null
            override fun onSubscribe(d: Disposable?) {
                saveNoteDisable = d
            }

            override fun onSuccess(t: Int?) {
                saveNoteDisable?.dispose()
            }

            override fun onError(e: Throwable?) {
                saveNoteDisable?.dispose()
                e?.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                }
            }
        }
    }

    private val initNote: SingleObserver<Note> by lazy {
        object : SingleObserver<Note> {
            private var initNoteDisable: Disposable? = null
            override fun onSubscribe(d: Disposable) {
                initNoteDisable = d
            }

            override fun onSuccess(note: Note) {
                _newNote.postValue(note)
                initNoteDisable?.dispose()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                initNoteDisable?.dispose()
            }
        }
    }

    internal fun initNote(nid: Long, type: InsertType? = null) {
        if (nid == NoteInfoActivity.INSERT) {
            _newNote.postValue(Note().apply {
                type ?: return@apply
                when (type) {
                    InsertType.CHECK_BOX -> {
                        tasks.apply {
                            add(Task())
                        }
                    }
                }
            })
        } else {
            isUpdate = true
            useCase.getNote(nid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(initNote)
        }
    }

    private var isUpdate = false

    private val _newNote: MutableLiveData<Note> by lazy { MutableLiveData<Note>() }
    internal val newNote: MutableLiveData<Note>
        get() = _newNote

    internal val images: MutableList<Bitmap> by lazy { mutableListOf() }
    internal val sounds: MutableList<Bitmap> by lazy { mutableListOf() }

    internal fun saveNote() {
        useCase.saveNote(_newNote.value!!, images, sounds, isUpdate = isUpdate)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(singleObserver)
    }
}