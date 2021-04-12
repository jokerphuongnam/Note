package com.example.note.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.LoginUseCase
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
class LoginViewModel @Inject constructor(private val useCase: LoginUseCase) : BaseViewModel() {

    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var loginDisable: Disposable? = null

    private val observerLogin: Consumer<User> by lazy {
        Consumer<User> { user ->
            _login.postValue(Resource.Success(user))
        }
    }

    private val _login: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    val login: MutableLiveData<Resource<User>>
        get() = _login

    fun loginEmailPass(username: String, password: String) {
        _login.postValue(Resource.Loading())
        loginDisable?.let {
            composite.remove(it)
            it.dispose()
        }
        loginDisable =
            useCase.loginEmailPass(username, password).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observerLogin, this::loginError)
        composite.add(loginDisable)
    }

    private fun loginError(t: Throwable) {
        when(t){
            is NoConnectivityException ->{
                 internetError.postValue("")
            }
            else ->{
                _login.postValue(Resource.Error(t.message ?: ""))
            }
        }
        t.printStackTrace()
    }
}