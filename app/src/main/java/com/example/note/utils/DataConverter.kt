package com.example.note.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date
import javax.inject.Inject

/**
 * for field have type not support for room, need class data converter
 * here set up type:
 * - to json (String) when save room
 * - to object when find from room
 * */
class DataConverter {
    @Inject
    lateinit var gson: Gson

    @TypeConverter
    fun fromStrings(strings: List<String>): String = gson.toJson(strings, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun toStrings(json: String): List<String> = gson.fromJson(json, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun fromDates(dates: List<Date>): String = gson.toJson(dates, object : TypeToken<List<Date>>() {}.type)

    @TypeConverter
    fun toDates(json: String): List<Date> = gson.fromJson(json, object : TypeToken<List<Date>>() {}.type)

    @TypeConverter
    fun fromDate(date: Date): String = gson.toJson(date, object : TypeToken<Date>() {}.type)

    @TypeConverter
    fun toDate(json: String): Date = gson.fromJson(json, object : TypeToken<Date>() {}.type)
}