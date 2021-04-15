package com.example.note.model.database.local.note

import com.example.note.model.database.domain.Note
import io.reactivex.rxjava3.core.Single

fun RoomNoteImpl.findSingleNote(nid: Long): Single<Note> =
    findNotesWithTask(nid).map { noteWithTask ->
        noteWithTask.toNote()
    }