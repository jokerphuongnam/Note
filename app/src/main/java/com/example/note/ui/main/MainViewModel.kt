package com.example.note.ui.main

import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.MainUseCase
import com.example.note.throwable.NoConnectivityException
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
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

    internal val uidLiveData: MutableLiveData<Resource<Long>>
        get() {
            _uid.postValue(Resource.Loading())
            uidDisable?.let {
                composite.remove(it)
                it.dispose()
            }
            uidDisable =
                useCase.currentUser().observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observerUid, this::onUidError)
            composite.add(uidDisable)
            return _uid
        }

    internal var uid: Long
        get() = TODO()
        set(value) {
            _uid.postValue(Resource.Success(value))
        }

    private var disposable: Disposable? = null

    private val _userLiveData: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    internal val userLiveData: MutableLiveData<Resource<User>>
        get() {
            _userLiveData.postValue(Resource.Loading())
            disposable?.let {
                it.dispose()
                composite.remove(it)
            }
            disposable = useCase.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    _userLiveData.postValue(Resource.Success(user))
                }, { throwable ->
                    throwable.printStackTrace()
                    _userLiveData.postValue(Resource.Error(throwable.message.toString()))
                }, {
                    _userLiveData.postValue(Resource.Success())
                })
            composite.add(disposable)
            return _userLiveData
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