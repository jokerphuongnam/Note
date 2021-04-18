package com.example.note.ui.main.userinfo

import androidx.lifecycle.MutableLiveData
import com.example.note.model.database.domain.User
import com.example.note.model.usecase.UserInfoUseCase
import com.example.note.ui.base.BaseViewModel
import com.example.note.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(private val useCase: UserInfoUseCase) :
    BaseViewModel() {
    private val composite: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private var disposable: Disposable? = null

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
            disposable?.let {
                it.dispose()
                composite.remove(it)
            }
            disposable = useCase.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    currentUser = user
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

    internal lateinit var currentUser: User

    internal lateinit var editUser: User

    internal fun editProfile() {
        _userLiveData.postValue(Resource.Loading())
    }

    override fun onCleared() {
        super.onCleared()
        composite.dispose()
    }
}