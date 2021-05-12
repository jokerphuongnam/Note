package com.example.note.model.usecase

import android.content.Context
import android.net.Uri
import com.example.note.model.database.domain.User
import com.example.note.model.repository.UserRepository
import com.example.note.ui.noteinfo.toMultipartBody
import com.example.note.utils.RetrofitUtils.AVATAR
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import javax.inject.Inject

class DefaultUserInfoUseCaseImpl @Inject constructor(
    override val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : UserInfoUseCase {

    override fun getUser(): Flowable<User> = userRepository.getUser().subscribeOn(Schedulers.io())

    override fun editProfile(user: User, avatar: Uri): Single<User> =
        userRepository.editProfile(user, avatar.toMultipartBody(AVATAR, context)).subscribeOn(Schedulers.io())

    override fun editProfile(user: User): Single<User> = userRepository.editProfile(user).subscribeOn(Schedulers.io())
}