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

    @ColumnInfo(name = "username")
    var username: String = ""

    var birthDayString: String
        get() {
            val calendar: Calendar = Calendar.getInstance()
            calendar.add(
                Calendar.MILLISECOND,
                TimeZone.getDefault().getOffset(calendar.timeInMillis)
            )
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdf.format(Date(birthDay * 1000))
        }
        set(value) {
            val calendar: Calendar = Calendar.getInstance()
            calendar.add(
                Calendar.MILLISECOND,
                TimeZone.getDefault().getOffset(calendar.timeInMillis)
            )
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            birthDay = sdf.parse(value)?.time ?: 0
        }
}