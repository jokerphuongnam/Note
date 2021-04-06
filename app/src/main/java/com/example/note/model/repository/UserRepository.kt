package com.example.note.model.repository

import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.user.UserNetwork
import javax.inject.Singleton

@Singleton
interface UserRepository {
    val local: UserLocal
    val network: UserNetwork
}