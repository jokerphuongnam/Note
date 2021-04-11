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
import com.example.note.utils.PagingUtil.LOOP
import com.example.note.utils.PagingUtil.PREFECT_DISTANCE
import com.example.note.utils.PagingUtil.START
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Singleton
class DefaultNoteRepositoryImpl @Inject constructor(
    override val local: NoteLocal,
    override val network: NoteNetwork,
    private val mediator: NoteRxMediator,
    private val currentUser: CurrentUser
) : NoteRepository {
    override fun insertNote(
        uid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Int> = Single.just(0)

    override fun insertTask(vararg tasks: Task): Single<Int> = Single.just(0)

    override fun updateNote(
        uid: Long,
        nid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Int> = Single.just(0)

    override fun updateTask(vararg tasks: Task): Single<Int> = Single.just(0)

    override fun deleteNote(uid: Long, nid: Long): Single<Int> = Single.just(0)

    override fun deleteTask(vararg tasks: Task): Single<Int> = Single.just(0)

    /**
     * get uid (is always non-null) in data store
     * get count note in api and return Single<Pair<Long, Long>> (uid, count note)
     * configure for Pager
     * convert to flowable
     * */
    override fun getNotes(): Flowable<PagingData<Note>> =
        currentUser.uid.flatMap { uid ->
            network.fetchCount(uid!!).map { count ->
                uid to count.body()!!
            }
        }.toFlowable().flatMap { uidToCount ->
            val (uid, count) = uidToCount
            Pager(
                config = PagingConfig(
                    pageSize = LOOP,
                    enablePlaceholders = true,
                    maxSize = count.toInt(),
                    prefetchDistance = PREFECT_DISTANCE,
                    initialLoadSize = START
                ),
                remoteMediator = mediator.apply {
                    maxCount = count
                    this.uid = uid
                },
                pagingSourceFactory = {
                    local.findNotes(uid)
                }
            ).flowable
        }
}