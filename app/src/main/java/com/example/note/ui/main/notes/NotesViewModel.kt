package com.example.note.ui.main.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.usecase.NotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val useCase: NotesUseCase) : ViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var noteDisable: Disposable? = null
    private val observerNotes: Consumer<PagingData<Note>> by lazy {
        Consumer<PagingData<Note>> { notes ->
            _noteLiveData.postValue(notes)
        }
    }

    private val _noteLiveData: MutableLiveData<PagingData<Note>> by lazy {
        MutableLiveData<PagingData<Note>>()
    }

    val noteLiveData: MutableLiveData<PagingData<Note>>
        get() {
            noteDisable?.let {
                composite.remove(it)
                it.dispose()
            }
            noteDisable = useCase.noteRepository.getNotes().observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerNotes, this::onNotesError)
            composite.add(noteDisable)
            return _noteLiveData
        }

    private fun onNotesError(error : Throwable){
        error.printStackTrace()
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}
