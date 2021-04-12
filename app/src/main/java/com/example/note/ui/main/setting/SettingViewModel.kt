package com.example.note.ui.main.setting

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.note.model.usecase.SettingUseCase
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val useCase: SettingUseCase) : BaseViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }


    private var logoutDisable: Disposable? = null
    private val logoutObserver: SingleObserver<Preferences> by lazy {
        object : SingleObserver<Preferences> {
            override fun onSubscribe(d: Disposable?) {
                logoutDisable = d
                composite.add(logoutDisable)
            }

            override fun onSuccess(t: Preferences?) {
                _logoutLiveData.postValue(Resource.Success())
            }

            override fun onError(e: Throwable?) {
                _logoutLiveData.postValue(Resource.Error(e?.message.toString()))
            }
        }
    }

    private val _logoutLiveData: MutableLiveData<Resource<PagingData<*>>> by lazy {
        MutableLiveData<Resource<PagingData<*>>>()
    }

    val logoutLiveData: MutableLiveData<Resource<PagingData<*>>> get() = _logoutLiveData

    fun logout() {
        _logoutLiveData.postValue(Resource.Loading())
        logoutDisable?.let {
            composite.remove(it)
            it.dispose()
        }
        useCase.logout().subscribeOn(AndroidSchedulers.mainThread()).subscribe(logoutObserver)
    }

}