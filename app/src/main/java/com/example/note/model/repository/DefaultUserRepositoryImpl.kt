package com.example.note.model.repository

import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.user.UserNetwork
import javax.inject.Inject

class DefaultUserRepositoryImpl @Inject constructor(override val local: UserLocal, override val network: UserNetwork) : UserRepository{

}