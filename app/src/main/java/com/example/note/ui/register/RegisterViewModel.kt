package com.example.note.ui.register

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.RegisterUseCase
import com.example.note.throwable.NotFoundException
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: RegisterUseCase
) : BaseViewModel() {
    internal val currentUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    init {
        currentUser.postValue(User())
    }

    private val _registerLiveData: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    internal val registerLiveData: MutableLiveData<Resource<User>> get() = _registerLiveData

    private val registerObserver: SingleObserver<User> by lazy {
        object : SingleObserver<User>{
            private var disposable: Disposable? = null
            override fun onSubscribe(d: Disposable) {
                disposable = d
                _registerLiveData.postValue(Resource.Loading())
            }

            override fun onSuccess(user: User) {
                _registerLiveData.postValue(Resource.Success(user))
            }

            override fun onError(e: Throwable?) {

                when (e) {
                    is NotFoundException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _registerLiveData.postValue(Resource.Error(""))
                    }
                }
            }
        }
    }

    internal var avatar: Bitmap? = null

    internal fun register(password: String) {
        useCase.register(currentUser.value!!, password, avatar).observeOn(AndroidSchedulers.mainThread())
            .subscribe(registerObserver)
    }
}