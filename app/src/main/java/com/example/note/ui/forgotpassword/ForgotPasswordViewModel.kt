package com.example.note.ui.forgotpassword

import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.ForgotPasswordUseCase
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
class ForgotPasswordViewModel @Inject constructor(private val useCase: ForgotPasswordUseCase): BaseViewModel() {

    private val _changePasswordLiveData: MutableLiveData<Resource<Int>> by lazy { MutableLiveData<Resource<Int>>() }

    internal val changePasswordLiveData: MutableLiveData<Resource<Int>> get() = _changePasswordLiveData

    private var changePasswordDisposable: Disposable? = null

    private val changePasswordObserver: SingleObserver<Int> by lazy {
        object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable) {
                _changePasswordLiveData.postValue(Resource.Loading())
                changePasswordDisposable = d
            }

            override fun onSuccess(uid: Int) {
                _changePasswordLiveData.postValue(Resource.Success(uid))
                changePasswordDisposable?.dispose()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                when (e) {
                    is NoConnectivityException -> {
                        internetError.postValue("")
                    }
                    is NotFoundException ->{
                        _changePasswordLiveData.postValue(Resource.Error("Cannot find username"))
                    }
                    else -> {
                    }
                }
                changePasswordDisposable?.dispose()
            }
        }
    }

    fun recoverPassword(username: String){
        useCase.forgotPassword(username).observeOn(AndroidSchedulers.mainThread()).subscribe(changePasswordObserver)
    }
}