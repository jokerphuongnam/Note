package com.example.note.model.database.local.note

import androidx.paging.PagingSource
import androidx.room.*
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.domain.supportquery.NoteWithTasks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface RoomNoteImpl : NoteLocal {
    /**
     * with show notes will do get demo notes for user follow
     * */
    @Query("SELECT note_id, title, detail, tags, is_favorite, tags, created_at, modified_at FROM NOTES WHERE user_id = :uid ORDER BY created_at")
    override fun findNotesPaging(uid: Long): PagingSource<Int, Note>

    /**
     * get first note for init notes
     * */
    @Query("SELECT note_id, title, detail, tags, is_favorite, tags, created_at, modified_at FROM NOTES WHERE user_id = :uid ORDER BY created_at DESC LIMIT 1")
    override fun findLastUpdateSingle(uid: Long): Single<Note>

    /**
     * when user select note user need seen all info note
     * */
    @Transaction
    @Query("SELECT * FROM NOTES WHERE note_id = :nid")
    fun findNotesWithTask(nid: Long): Single<NoteWithTasks>

    /**
     * make noteWithTask do support query for note
     * */
    override fun findSingleNote(nid: Long): Single<Note> = findNotesWithTask(nid).map {
        it.toNote()
    }


    @Query("SELECT * FROM TASKS WHERE note_id =:nid")
    override fun findTasksByUid(nid: Long): Single<List<Task>>

    override fun insertNotesWithTime(notes: List<Note>) {
        notes.map { note ->
            note.apply {
                createAt = System.currentTimeMillis()
                modifiedAt = System.currentTimeMillis()
            }
        }
        insertNotes(notes)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertNotes(notes: List<Note>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insertTasks(vararg tasks: Task)

    @Update
    override fun updateNotes(vararg notes: Note): Single<Int>

    @Update
    override fun updateTasks(vararg tasks: Task): Single<Int>

    @Delete
    override fun deleteNotes(vararg notes: Note)

    @Query("DELETE FROM NOTES WHERE user_id = :uid")
    override fun clearNotesByUserId(uid: Long)

    @Delete
    override fun deleteTasks(vararg tasks: Task)

    @Query("DELETE FROM TASKS WHERE note_id = :uid")
    override fun clearTasksByNote(uid: Long): Single<Int>
}