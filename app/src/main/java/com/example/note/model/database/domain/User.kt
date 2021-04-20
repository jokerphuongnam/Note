package com.example.note.model.database.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val uid: Long,
    @ColumnInfo(name = "first_name") var fname: String,
    @ColumnInfo(name = "last_name") var lname: String,
    @ColumnInfo(name = "avatar") var avatar: String?,
    @ColumnInfo(name = "birth_day") var birthDay: Long
) {


    constructor() : this(0, "", "", null, 916678800000)

    @ColumnInfo(name = "username")
    var username: String = ""

    val birthDayCalendar: Calendar
        get() {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getDefault()
            calendar.timeInMillis = birthDay
            return calendar
        }

    var birthDayString: String
        get() {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(birthDayCalendar.time)
        }
        set(value) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()
            birthDay = dateFormat.parse(value)!!.time
        }

    fun clone(): User {
        val user = this.copy()
        user.username = username
        return user
    }
}