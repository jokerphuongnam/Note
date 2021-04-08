package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.IGNORE
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.domain.supportquery.NoteWithTasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomNoteImpl @Inject constructor(private val dao: NoteDao) : NoteLocal {

    override fun findNotes(uid: Long): PagingSource<Int, Note> = dao.findNotes(uid)

    override fun findNote(nid: Long): Single<Note> = dao.findNote(nid).map { noteWithTask ->
        noteWithTask.note.apply {
            tasks = noteWithTask.tasks
        }
    }

    override fun insertNotes(vararg notes: Note): Completable = dao.insertNotes(*notes)

    override fun insertNotesWithTimestamp(vararg notes: Note):  Completable =
        dao.insertNotes(*notes.map { note ->
            note.apply {
                modifiedAt = System.currentTimeMillis()
            }
        }.toTypedArray())

    override fun insertTasks(vararg tasks: Task) = dao.insertTasks(*tasks)

    override fun updateNotes(vararg notes: Note): Single<Int> = dao.updateNotes(*notes)

    override fun updateTasks(vararg tasks: Task): Single<Int> = dao.updateTasks(*tasks)

    override fun deleteNotes(vararg notes: Note): Single<Int> = dao.deleteNotes(*notes)

    override fun deleteNotes(uid: Long): Single<Int> = dao.deleteNotes(uid)

    override fun deleteTasks(vararg tasks: Task): Single<Int> = dao.deleteTasks(*tasks)

    @Dao
    interface NoteDao {
        @Transaction
        @Query("SELECT note_id, title, detail, tags, is_favorite, tags, modified_at FROM NOTES WHERE user_id = :uid")
        fun findNotes(uid: Long): PagingSource<Int, Note>

        @Query("SELECT * FROM NOTES WHERE note_id = :nid")
        fun findNote(nid: Long): Single<NoteWithTasks>

        @Insert(onConflict = IGNORE)
        fun insertNotes(vararg notes: Note): Completable

        @Insert(onConflict = IGNORE)
        fun insertTasks(vararg tasks: Task)

        @Update
        fun updateNotes(vararg notes: Note): Single<Int>

        @Update
        fun updateTasks(vararg tasks: Task): Single<Int>

        @Delete
        fun deleteNotes(vararg notes: Note): Single<Int>

        @Query("DELETE FROM NOTES WHERE user_id = :uid")
        fun deleteNotes(uid: Long): Single<Int>

        @Delete
        fun deleteTasks(vararg task: Task): Single<Int>
    }
}