package com.example.note.ui.main.notes

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.note.model.database.domain.Note
import com.example.note.model.usecase.NotesUseCase
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val useCase: NotesUseCase) : BaseViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var noteDisable: Disposable? = null
    private val observerNotes: Consumer<PagingData<Note>> by lazy {
        Consumer<PagingData<Note>> { notes ->
            _noteLiveData.postValue(Resource.Success(notes))
        }
    }

    private val _noteLiveData: MutableLiveData<Resource<PagingData<Note>>> by lazy {
        MutableLiveData<Resource<PagingData<Note>>>()
    }

    /**
     * first time loading
     * check null noteDisable if (noteDisable != null) remove in composite and dispose this
     * handle it have data and error
     * add noteDisable to composite
     * */
    val noteLiveData: MutableLiveData<Resource<PagingData<Note>>>
        get() {
            _noteLiveData.postValue(Resource.Loading())
            noteDisable?.let {
                composite.remove(it)
                it.dispose()
            }
            noteDisable =
                useCase.getNotes().subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(observerNotes, this::onNotesError)
            composite.add(noteDisable)
            return _noteLiveData
        }

    private fun onNotesError(error: Throwable) {
        error.printStackTrace()
        _noteLiveData.postValue(Resource.Error(error.message!!))
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}
