package com.example.note.ui.main.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.example.note.model.database.domain.Note
import com.example.note.model.usecase.NotesUseCase
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
     * when call userLiveData for set observer will action
     * set loading for livedata
     * disposable
     * remove disposable from composite
     * observer from retrofit
     * add disposable to composite
     * */
    internal val noteLiveData: MutableLiveData<Resource<PagingData<Note>>>
        get() {
            _noteLiveData.postValue(Resource.Loading())
            noteDisable?.let {
                composite.remove(it)
                it.dispose()
            }
            noteDisable =
                useCase.getNotes()
                    .cachedIn(viewModelScope)
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

    private var dispose : Disposable? = null

    /**
     * when call userLiveData for set observer will action
     * set loading for livedata
     * disposable
     * observer from retrofit
     * when success or error will dispose
     * */
    private val deleteObserver: SingleObserver<Long> by lazy {
        object : SingleObserver<Long>{
            override fun onSubscribe(d: Disposable) {
                dispose?.let {
                    composite.remove(it)
                    it.dispose()
                }
                dispose = d
            }

            override fun onSuccess(t: Long?) {
                deleteLiveData.postValue(Resource.Success(0))
                dispose?.dispose()
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                deleteLiveData.postValue(Resource.Error(""))
                dispose?.dispose()
            }
        }
    }

    internal val deleteLiveData: MutableLiveData<Resource<Long>> by lazy {
        MutableLiveData<Resource<Long>>()
    }

    internal fun delete(note: Note) {
        deleteLiveData.postValue(Resource.Loading())
        useCase.deleteNote(note).observeOn(AndroidSchedulers.mainThread()).subscribe(deleteObserver)
    }
}
