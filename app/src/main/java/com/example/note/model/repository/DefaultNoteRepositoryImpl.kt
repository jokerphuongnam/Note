package com.example.note.model.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.throwable.CannotSaveException
import com.example.note.utils.PagingUtil.INIT_LOAD_SIZE
import com.example.note.utils.PagingUtil.PAGE_SIZE
import com.example.note.utils.PagingUtil.PREFECT_DISTANCE
import com.example.note.utils.RetrofitConstrain.INTERNAL_SERVER_ERROR
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DefaultNoteRepositoryImpl @Inject constructor(
    override val local: NoteLocal,
    override val network: NoteNetwork,
    private val mediator: NoteRxMediator
) : NoteRepository {
    /**
     * insert to network
     * clear tasks by note
     * insert tasks (list was changed) for note
     * insert note
     * */
    override fun insertNote(
        note: Note,
        images: List<MultipartBody.Part>?,
        sounds: List<MultipartBody.Part>?
    ): Single<Int> =
        network.insertNote(note, images ?: emptyList(), sounds ?: emptyList()).flatMap {
            if (it.code() == INTERNAL_SERVER_ERROR) {
                throw CannotSaveException()
            } else {
                val receiveNote: Note = it.body()!!
                receiveNote.tasks.forEach { task: Task ->
                    task.noteId = receiveNote.nid
                }
                local.clearTasksByNote(receiveNote.nid).flatMap {
                    insertTasks(*receiveNote.tasks.toTypedArray()).flatMap {
                        local.insertNotes(mutableListOf(receiveNote))
                        Single.just(0)
                    }
                }
            }
        }

    override fun insertTasks(vararg tasks: Task): Single<Int> = Single.just(0)

    /**
     * update to network
     * clear task by note
     * insert task (list was changed) for note
     * update note
     * */
    override fun updateNote(
        note: Note,
        images: List<MultipartBody.Part>?,
        sounds: List<MultipartBody.Part>?
    ): Single<Int> =
        network.updateNote(note, images ?: emptyList(), sounds ?: emptyList()).flatMap {
            if (it.code() == INTERNAL_SERVER_ERROR) {
                throw CannotSaveException()
            } else {
                val receiveNote: Note = it.body()!!
                receiveNote.tasks.forEach { task: Task ->
                    task.noteId = receiveNote.nid
                }
                local.clearTasksByNote(receiveNote.nid).flatMap {
                    insertTasks(*receiveNote.tasks.toTypedArray()).flatMap {
                        local.updateNotes(receiveNote)
                    }
                }
            }
        }

    override fun updateTask(vararg tasks: Task): Single<Int> = Single.just(0)

    override fun deleteNote(uid: Long, nid: Long): Single<Int> = Single.just(0)

    override fun deleteTask(vararg tasks: Task): Single<Int> = local.deleteTasks(*tasks)

    override fun clearTasksByNote(nid: Long): Single<Int> = local.clearTasksByNote(nid)

    /**
     * get uid (is always non-null) in data store
     * get count note in api and return Single<Pair<Long, Long>> (uid, count note)
     * configure for Pager
     * convert to flowable
     * */
    override fun getNotes(uid: Long): Flowable<PagingData<Note>> =
        network.fetchCount(uid).map { count ->
            count.body()!!
        }.toFlowable().flatMap { count ->
            val remoteMediator = mediator.apply {
                maxCount = count
                this.uid = uid
            }
            val pagingSourceFactory = {
                val findNotes = local.findNotesPaging(uid)
                findNotes
            }
            if (count == 0L || count < PAGE_SIZE + 2 * PREFECT_DISTANCE) {
                /**
                 * if count == 0 no need divide
                 * no need to pass max into the pager
                 *
                 * if count != 0 need divide
                 * max page need than page size * prefect distance
                 * */
                Pager(
                    config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = true,
                        prefetchDistance = PREFECT_DISTANCE,
                        initialLoadSize = INIT_LOAD_SIZE
                    ),
                    remoteMediator = remoteMediator,
                    pagingSourceFactory = pagingSourceFactory
                )
            } else {
                Pager(
                    config = PagingConfig(
                        pageSize = PAGE_SIZE,
                        enablePlaceholders = true,
                        maxSize = count.toInt(),
                        prefetchDistance = PREFECT_DISTANCE,
                        initialLoadSize = INIT_LOAD_SIZE
                    ),
                    remoteMediator = remoteMediator,
                    pagingSourceFactory = pagingSourceFactory
                )
            }.flowable
        }

    override fun getSingleNote(uid: Long): Single<Note> = local.findSingleNote(uid)

    override fun clearNotes(uid: Long): Single<Int> = local.deleteNotes(uid)
}