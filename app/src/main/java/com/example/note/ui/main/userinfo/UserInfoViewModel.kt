package com.example.note.ui.main.userinfo

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.UserInfoUseCase
import com.example.note.throwable.NotFoundException
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(private val useCase: UserInfoUseCase) :
    BaseViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var userDisposable: Disposable? = null

    private val _userLiveData: MutableLiveData<Resource<User>> by lazy {
        MutableLiveData<Resource<User>>()
    }

    /**
     * when call userLiveData for set observer will action
     * set loading for livedata
     * disposable
     * remove disposable from composite
     * observer from retrofit
     * add disposable to composite
     * */
    internal val userLiveData: MutableLiveData<Resource<User>>
        get() {
            _userLiveData.postValue(Resource.Loading())
            userDisposable?.let {
                it.dispose()
                composite.remove(it)
            }
            userDisposable = useCase.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    currentUser.postValue(user)
                    _userLiveData.postValue(Resource.Success(user))
                }, { throwable ->
                    throwable.printStackTrace()
                    _userLiveData.postValue(Resource.Error(throwable.message.toString()))
                }, {
                    _userLiveData.postValue(Resource.Success())
                })
            composite.add(userDisposable)
            return _userLiveData
        }

    internal val currentUser: MutableLiveData<User> by lazy { MutableLiveData<User>() }

    internal lateinit var tempUser: User

    private var editProfileDisposable: Disposable? = null

    private val _resultEditUserLiveData: MutableLiveData<Resource<User>> by lazy { MutableLiveData<Resource<User>>() }

    internal val resultEditUserLiveData: MutableLiveData<Resource<User>> get() = _resultEditUserLiveData

    private val resultEditUserObservable: SingleObserver<User> by lazy {
        object : SingleObserver<User> {
            override fun onSubscribe(d: Disposable) {
                _userLiveData.postValue(Resource.Loading())
                editProfileDisposable?.let {
                    it.dispose()
                    composite.remove(it)
                }
                editProfileDisposable = d
                composite.add(userDisposable)
            }

            override fun onSuccess(user: User) {
                _resultEditUserLiveData.postValue(Resource.Success(user))
                editProfileDisposable?.let {
                    it.dispose()
                    composite.remove(it)
                }
            }

            override fun onError(e: Throwable) {
                when (e) {
                    is NotFoundException -> {
                        internetError.postValue("")
                    }
                    else -> {
                        _resultEditUserLiveData.postValue(Resource.Error(""))
                    }
                }
                editProfileDisposable?.let {
                    it.dispose()
                    composite.remove(it)
                }
            }

        }
    }

    private var avatar: Bitmap? = null

    internal fun editProfile() {
        useCase.editProfile(currentUser.value!!, avatar).observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultEditUserObservable)
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}