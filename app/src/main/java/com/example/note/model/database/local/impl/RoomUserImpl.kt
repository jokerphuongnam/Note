package com.example.note.model.database.local.impl

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.note.model.database.domain.User
import com.example.note.model.database.local.impl.UserLocal
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface RoomUserImpl : UserLocal {

    @Query("SELECT * FROM USERS")
    override fun findUsers(): Single<List<User>>

    @Query("SELECT * FROM USERS WHERE user_id = :uid")
    override fun findSingleUser(uid: Long): Flowable<User>

    @Insert(onConflict = REPLACE)
    override fun insertUser(users: User): Single<Long>

    @Update
    override fun updateUsers(vararg users: User)

    @Delete
    override fun deleteUsers(vararg users: User): Single<Int>

    @Query("DELETE FROM USERS WHERE user_id = :uid")
    override fun deleteUser(uid: Long): Single<Int>
}