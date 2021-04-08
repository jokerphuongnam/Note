package com.example.note.model.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.User
import com.example.note.utils.DataConverter
import com.example.note.model.database.local.note.RoomNoteImpl
import com.example.note.model.database.local.user.RoomUserImpl
import com.example.note.utils.RoomConstrain.DB_VER
import javax.inject.Singleton

@Singleton
@Database(entities = [Note::class, User::class], version = DB_VER)
@TypeConverters(DataConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): RoomUserImpl.UserDao
    abstract fun noteDao(): RoomNoteImpl.NoteDao
}