package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import androidx.room.*
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.domain.supportquery.NoteWithTasks
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RoomNoteImpl @Inject constructor(private val dao: NoteDao): NoteLocal {

    override fun findNotes(uid: Long): PagingSource<Int, Note> = dao.findNotes(uid)

    override fun findNote(nid: Long): Single<NoteWithTasks> = dao.findNote(nid)

    override fun insertNotes(vararg notes: Note): Single<Int> = dao.insertNotes(*notes)

    override fun insertTasks(vararg tasks: Task): Single<Int> = dao.insertTasks(*tasks)

    override fun updateNotes(vararg notes: Note): Single<Int> = dao.updateNotes(*notes)

    override fun updateTasks(vararg tasks: Task): Single<Int> = dao.updateTasks(*tasks)

    override fun deleteNotes(vararg notes: Note): Single<Int> = dao.deleteNotes(*notes)

    override fun deleteTasks(vararg tasks: Task): Single<Int> = dao.deleteTasks(*tasks)

    @Dao
    interface NoteDao {

        @Query("SELECT title, detail, tags FROM NOTES WHERE user_id = :uid")
        fun findNotes(uid: Long): PagingSource<Int, Note>

        @Query("SELECT * FROM NOTES WHERE note_id = :nid")
        fun findNote(nid: Long): Single<NoteWithTasks>

        @Insert
        fun insertNotes(vararg notes: Note): Single<Int>

        @Insert
        fun insertTasks(vararg tasks: Task): Single<Int>

        @Update
        fun updateNotes(vararg notes: Note): Single<Int>

        @Update
        fun updateTasks(vararg tasks: Task): Single<Int>

        @Delete
        fun deleteNotes(vararg notes: Note): Single<Int>

        @Delete
        fun deleteTasks(vararg task: Task): Single<Int>
    }
}