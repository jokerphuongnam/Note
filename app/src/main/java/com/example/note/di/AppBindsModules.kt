package com.example.note.di

import androidx.paging.ExperimentalPagingApi
import com.example.note.model.database.local.LocalReference
import com.example.note.model.database.local.impl.DataStoreCurrentUserImpl
import com.example.note.model.database.local.CurrentUser
import com.example.note.model.database.network.NetworkConnectionInterceptor
import com.example.note.model.database.network.NoteNetwork
import com.example.note.model.database.network.impl.NoteRetrofitServiceImpl
import com.example.note.model.database.network.UserNetwork
import com.example.note.model.database.network.impl.UserRetrofitServiceImpl
import com.example.note.model.repository.*
import com.example.note.model.repository.impl.DefaultNoteRepositoryImpl
import com.example.note.model.repository.impl.DefaultUserRepositoryImpl
import com.example.note.model.usecase.*
import com.example.note.model.usecase.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Interceptor

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModules {
    //interceptor
    @Binds
    abstract fun getInterceptor(interceptor: NetworkConnectionInterceptor): Interceptor

    //database
    @Binds
    abstract fun getSettingLocal(local: LocalReference): LocalReference

    @Binds
    abstract fun getCurrentUser(local: DataStoreCurrentUserImpl): CurrentUser

    @Binds
    abstract fun getNoteNetwork(network: NoteRetrofitServiceImpl): NoteNetwork

    @Binds
    abstract fun getUserNetwork(network: UserRetrofitServiceImpl): UserNetwork

    //repository
    @Binds
    abstract fun getNoteRepository(repository: DefaultNoteRepositoryImpl): NoteRepository

    @Binds
    abstract fun getUserRepository(repository: DefaultUserRepositoryImpl): UserRepository

    //use case
    @Binds
    abstract fun getMainUseCase(useCase: DefaultMainUseCaseImpl): MainUseCase

    @Binds
    abstract fun getLoginUseCase(useCase: DefaultLoginUseCaseImpl): LoginUseCase

    @Binds
    abstract fun getEditNoteUseCase(useCase: DefaultEditNoteUseCaseImpl): EditNoteUseCase

    @Binds
    abstract fun getNoteInfoUseCase(useCase: DefaultNoteInfoUseCaseImpl): NoteInfoUseCase

    @Binds
    abstract fun getNotesUseCase(useCase: DefaultNotesUseCaseImpl): NotesUseCase

    @Binds
    abstract fun getSettingUseCase(useCase: DefaultSettingUseCaseImpl): SettingUseCase

    @Binds
    abstract fun getUserInfoUseCase(useCase: DefaultUserInfoUseCaseImpl): UserInfoUseCase

    @Binds
    abstract fun getForgotPasswordUseCase(useCase: DefaultForgotPasswordUseCaseImpl): ForgotPasswordUseCase

    @Binds
    abstract fun getChangePasswordUseCase(useCase: DefaultChangePasswordUseCaseImpl): ChangePasswordUseCase

    @Binds
    abstract fun getRegisterUseCase(useCase: DefaultRegisterUseCaseImpl): RegisterUseCase
}