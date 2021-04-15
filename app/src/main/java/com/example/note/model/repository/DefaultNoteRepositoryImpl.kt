package com.example.note.model.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.local.user.CurrentUser
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.throwable.CannotSaveException
import com.example.note.utils.PagingUtil.PAGE_SIZE
import com.example.note.utils.PagingUtil.PREFECT_DISTANCE
import com.example.note.utils.PagingUtil.INIT_LOAD_SIZE
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
    private val mediator: NoteRxMediator,
    private val currentUser: CurrentUser
) : NoteRepository {
    override fun insertNote(
        note: Note,
        images: List<MultipartBody.Part>?,
        sounds: List<MultipartBody.Part>?
    ): Single<Int> =
        network.insertNote(note, images ?: emptyList(), sounds ?: emptyList()).flatMap {
            if (it.code() == 200) {
                throw CannotSaveException()
            } else {
                val receiveNote: Note = it.body()!!
                local.clearTasksByNote(receiveNote.nid).flatMap {
                    local.insertNotes(receiveNote).toSingle {
                        0
                    }
                }
            }
        }

    override fun insertTask(vararg tasks: Task): Single<Int> = Single.just(0)

    override fun updateNote(
        nid: Long,
        note: Note,
        images: List<MultipartBody.Part>?,
        sounds: List<MultipartBody.Part>?
    ): Single<Int> = Single.just(0)

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
                local.findNotes(uid)
            }
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
            ).flowable
        }

    override fun getSingleNote(): Single<Note> = currentUser.uid.firstOrError().flatMap { uid ->
        local.findSingleNote(uid!!)
    }
}