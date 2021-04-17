package com.example.note.model.database.local.user

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Dao
interface RoomUserImpl : UserLocal {

    @Query("SELECT * FROM USERS")
    override fun findUsers(): Single<List<User>>

    @Insert(onConflict = REPLACE)
    override fun insertUser(users: User): Single<Long>

    @Update
    override fun updateUsers(vararg users: User): Single<Int>

    @Delete
    override fun deleteUsers(vararg users: User): Single<Int>

    @Query("DELETE FROM USERS WHERE user_id = :uid")
    override fun deleteUser(uid: Long): Single<Int>
}