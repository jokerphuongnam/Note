package com.example.note.model.database.local.user

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomUserImpl @Inject constructor(private val dao: UserDao): UserLocal {

    override fun findUsers(): Single<List<User>> {
        TODO("Not yet implemented")
    }

    override fun insertUsers(vararg user: User): Int {
        TODO("Not yet implemented")
    }

    override fun updateUsers(vararg user: User): Int {
        TODO("Not yet implemented")
    }

    override fun deleteUsers(vararg user: User): Int {
        TODO("Not yet implemented")
    }

    interface UserDao{

    }
}