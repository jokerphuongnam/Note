package com.example.note.model.repository

import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.network.note.NoteNetwork
import javax.inject.Inject

class DefaultNoteRepositoryImpl @Inject constructor(override val local: NoteLocal, override val network: NoteNetwork) : NoteRepository