package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.domain.supportquery.NoteWithTasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Dao
interface RoomNoteImpl : NoteLocal {
    /**
     * with show notes will do get demo notes for user follow
     * */
    @Query("SELECT note_id, title, detail, tags, is_favorite, tags, created_at, modified_at FROM NOTES WHERE user_id = :uid")
    override fun findNotes(uid: Long): PagingSource<Int, Note>

    /**
     * when user select note user need seen all info note
     * */
    @Transaction
    @Query("SELECT * FROM NOTES WHERE note_id = :nid")
    fun findNotesWithTask(nid: Long): Single<NoteWithTasks>

    @Query("SELECT * FROM NOTES WHERE note_id = :nid")
    override fun findSingleNote(nid: Long): Single<Note>

    @Insert(onConflict = REPLACE)
    override fun insertNotes(vararg notes: Note): Completable


    @Insert(onConflict = REPLACE)
    override fun insertTasks(vararg tasks: Task)

    @Update
    override fun updateNotes(vararg notes: Note): Single<Int>

    @Update
    override fun updateTasks(vararg tasks: Task): Single<Int>

    @Delete
    override fun deleteNotes(vararg notes: Note): Single<Int>

    @Query("DELETE FROM NOTES WHERE user_id = :uid")
    override fun deleteNotes(uid: Long): Single<Int>

    @Delete
    override fun deleteTasks(vararg tasks: Task): Single<Int>
}