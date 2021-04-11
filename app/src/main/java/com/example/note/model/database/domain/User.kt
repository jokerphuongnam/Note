package com.example.note.model.database.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val uid: Long,
    @ColumnInfo(name = "first_name") var fname: String,
    @ColumnInfo(name = "last_name") var lname: String,
    @ColumnInfo(name = "avatar") var avatar: String?,
    @ColumnInfo(name = "birth_day") var birthDay: Date
){
    @ColumnInfo(name = "username") var username:String = ""
}