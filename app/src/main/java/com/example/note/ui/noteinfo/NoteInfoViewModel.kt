package com.example.note.ui.noteinfo

import android.util.Log
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.usecase.NoteInfoUseCase
import com.example.note.throwable.NoConnectivityException
import com.example.note.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class NoteInfoViewModel @Inject constructor(private val useCase: NoteInfoUseCase) :
    BaseViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

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

    private var _newNote: Note? = null
    var newNote: Note
        get() = _newNote!!
        set(value) {
            _newNote = value
        }

    fun saveNote() {
        useCase.saveNote(_newNote!!).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(singleObserver)
    }
}