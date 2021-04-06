package com.example.note.model.repository

import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.network.note.NoteNetwork
import javax.inject.Singleton

@Singleton
interface NoteRepository {
    val local: NoteLocal
    val network: NoteNetwork
}