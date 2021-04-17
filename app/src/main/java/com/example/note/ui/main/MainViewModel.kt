package com.example.note.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.Note
import com.example.note.model.usecase.MainUseCase
import com.example.note.throwable.NoConnectivityException
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: MainUseCase) : BaseViewModel() {

    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var uidDisable: Disposable? = null
    private val observerUid: Consumer<Long> by lazy {
        Consumer<Long> { uid ->
            _uid.postValue(Resource.Success(uid))
        }
    }

    private val _uid: MutableLiveData<Resource<Long>> by lazy {
        MutableLiveData<Resource<Long>>()
    }

    val uidLiveData: MutableLiveData<Resource<Long>>
        get() {
            _uid.postValue(Resource.Loading())
            uidDisable?.let {
                composite.remove(it)
                it.dispose()
            }
            uidDisable =
                useCase.currentUser().subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(observerUid, this::onUidError)
            composite.add(uidDisable)
            return _uid
        }

    var uid: Long
        get() = TODO()
        set(value) {
            _uid.postValue(Resource.Success(value))
        }

    private fun onUidError(error: Throwable) {
        error.printStackTrace()
        when (error) {
            is NoConnectivityException -> {
                internetError.postValue("")
            }
            else -> {
                _uid.postValue(Resource.Error(error.message!!))
            }
        }
    }
}