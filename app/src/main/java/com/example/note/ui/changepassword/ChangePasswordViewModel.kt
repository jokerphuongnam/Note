package com.example.note.ui.changepassword

import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.ChangePasswordUseCase
import com.example.note.throwable.NoConnectivityException
import com.example.note.throwable.NotFoundException
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val useCase: ChangePasswordUseCase
) : BaseViewModel() {

    private val _checkPasswordLiveData: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    internal val checkPasswordLiveData: MutableLiveData<Resource<User>> get() = _checkPasswordLiveData

    private var checkPasswordDisposable: Disposable? = null

    private val checkPasswordObserver: SingleObserver<User> by lazy {
        object : SingleObserver<User> {
            override fun onSubscribe(d: Disposable) {
                _checkPasswordLiveData.postValue(Resource.Loading())
                checkPasswordDisposable = d
            }

            override fun onSuccess(user: User) {
                _checkPasswordLiveData.postValue(Resource.Success(user))
                checkPasswordDisposable?.dispose()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    is NotFoundException ->{
                        _checkPasswordLiveData.postValue(Resource.Error(""))
                    }
                    else -> {
                    }
                }
                checkPasswordDisposable?.dispose()
            }
        }
    }

    internal fun checkOldPassword(oldPassword: String) {
        useCase.checkOldPassword(oldPassword).observeOn(AndroidSchedulers.mainThread()).subscribe(checkPasswordObserver)
    }

    private val _changePasswordLiveData: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    internal val changePasswordLiveData: MutableLiveData<Resource<User>> get() = _changePasswordLiveData

    private var changePasswordDisposable: Disposable? = null

    private val changePasswordObserver: SingleObserver<User> by lazy {
        object : SingleObserver<User> {
            override fun onSubscribe(d: Disposable) {
                _changePasswordLiveData.postValue(Resource.Loading())
                changePasswordDisposable = d
            }

            override fun onSuccess(user: User) {
                _changePasswordLiveData.postValue(Resource.Success(user))
                changePasswordDisposable?.dispose()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    is NotFoundException ->{
                        _changePasswordLiveData.postValue(Resource.Error(""))
                    }
                    else -> {
                    }
                }
                changePasswordDisposable?.dispose()
            }
        }
    }

    internal fun changePassword(oldPassword: String, newPassword: String){
        useCase.changePassword(oldPassword, newPassword).observeOn(AndroidSchedulers.mainThread()).subscribe(changePasswordObserver)
    }
}