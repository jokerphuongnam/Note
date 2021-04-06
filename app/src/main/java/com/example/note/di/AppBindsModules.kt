package com.example.note.di

import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.local.note.RoomNoteImpl
import com.example.note.model.database.local.user.RoomUserImpl
import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.model.database.network.note.NoteRetrofitServiceImpl
import com.example.note.model.database.network.user.UserNetwork
import com.example.note.model.database.network.user.UserRetrofitServiceImpl
import com.example.note.model.repository.*
import com.example.note.model.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModules {
    //database
    @Binds
    abstract fun getNoteNetwork(network: NoteRetrofitServiceImpl): NoteNetwork

    @Binds
    abstract fun getUserNetwork(network: UserRetrofitServiceImpl): UserNetwork

    @Binds
    abstract fun getNoteLocal(local: RoomNoteImpl): NoteLocal

    @Binds
    abstract fun getUserLocal(local: RoomUserImpl): UserLocal

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
}