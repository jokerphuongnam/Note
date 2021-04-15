package com.example.note.ui.noteinfo

import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.Task
import com.example.note.model.usecase.NoteInfoUseCase
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class NoteInfoViewModel @Inject constructor(private val infoUseCase: NoteInfoUseCase) :
    BaseViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    private var deleteDisable: Disposable? = null
    private val deleteTaskObserver by lazy {
        object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable?) {
                delete.postValue(Resource.Loading())
                deleteDisable?.let {
                    composite.remove(it)
                    it.dispose()
                }
                deleteDisable = d
            }

            override fun onSuccess(t: Int?) {
                delete.postValue(Resource.Success(!delete.value!!.data!!))
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                delete.postValue(Resource.Error(e.message!!))
            }
        }
    }

    val delete: MutableLiveData<Resource<Boolean>> by lazy {
        MutableLiveData<Resource<Boolean>>()
    }

    fun deleteTask(vararg task: Task) {
        infoUseCase.deleteTask(*task).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(deleteTaskObserver)
    }
}