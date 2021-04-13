package com.example.note.utils

import java.text.SimpleDateFormat
import java.util.*


fun Long.toDate(): Date {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    dateFormat.numberFormat
    return Date(this * 1000)
}