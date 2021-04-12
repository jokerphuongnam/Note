package com.example.note.model.database.local.user

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomUserImpl @Inject constructor(private val dao: UserDao): UserLocal {

    override fun findUsers(): Single<List<User>> = dao.findUsers()

    override fun insertUsers(users: User): Single<Long> = dao.insertUsers(users)

    override fun updateUsers(vararg users: User): Single<Int> = dao.updateUsers(*users)

    override fun deleteUsers(vararg users: User): Single<Int> = dao.deleteUsers(*users)

    override fun deleteUser(uid: Long): Single<Int> = dao.deleteUser(uid)

    @Dao
    interface UserDao{
        @Query("SELECT * FROM USERS")
        fun findUsers(): Single<List<User>>

        @Insert(onConflict = REPLACE)
        fun insertUsers(users: User): Single<Long>

        @Update
        fun updateUsers(vararg users: User): Single<Int>

        @Delete
        fun deleteUsers(vararg users: User): Single<Int>

        @Query("DELETE FROM USERS WHERE user_id = :uid")
        fun deleteUser(uid: Long): Single<Int>
    }
}