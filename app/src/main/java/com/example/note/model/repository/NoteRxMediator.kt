package com.example.note.model.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.example.note.model.database.domain.Note
import com.example.note.model.database.local.AppDatabase
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.utils.PagingUtil
import com.example.note.utils.PagingUtil.OUT_DATE_TIME_STAMP
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalPagingApi
class NoteRxMediator @Inject constructor(
    private val local: NoteLocal,
    private val network: NoteNetwork,
    private val database: AppDatabase
) : RxRemoteMediator<Int, Note>() {
    /**
     * maxCount will be transmission in repository
     * */
    private var _maxCount: Long = 0
    var maxCount: Long
        get() = _maxCount
        set(value) {
            _maxCount = value
        }
    private var _uid: Long = 0

    /**
     * uid will be transmission in repository
     * */
    var uid: Long?
        get() = _uid
        set(value) {
            _uid = value!!
        }

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Note>
    ): Single<MediatorResult> = Single.just(loadType).observeOn(Schedulers.io()).map {
        /**
         * if loadType == refresh is user want to refresh note => page == null
         * prepend almost not happening
         * append return simple page
         * */
        when (loadType) {
            REFRESH -> {
                null
            }
            APPEND -> {
                getRemoteKeyForLastItem(state)
            }
            PREPEND -> {
                getRemoteKeyForFirstItem(state)
            }
        }
    }.flatMap { page ->
        val firstNote = state.firstItemOrNull()
        var type = loadType
        /**
         * - page == null: user want to refresh notes
         * - firstNote == null: note not cached yet
         * - System.currentTimeMillis() - firstNote.modifiedAt: data outdated
         * will delete all data of user in cache and set type = refresh to save extend time
         * */
        if (page == null || firstNote == null || System.currentTimeMillis() - firstNote.modifiedAt > OUT_DATE_TIME_STAMP) {
            local.deleteNotes(_uid)
            type = REFRESH
        }
        /**
         * convert page to index of first, and element page
         * */
        val (start, end) = PagingUtil.pageToItem(page ?: 1)
        network.fetchNotes(_uid, start, end).map { notes ->
            /**
             * fetch notes and insert to cache depend type and if can't fetch notes in api will create emptyList
             * */
            insertToDb(type, notes.body() ?: emptyList())
        }.map<MediatorResult> {
            /**
             * if end >= maxCount - 1: end page user will don't need fetch more notes
             * */
            MediatorResult.Success(end >= maxCount - 1)
        }.onErrorReturn {
            MediatorResult.Error(it)
        }
    }.onErrorReturn { MediatorResult.Error(it) }

    /**
     * check loadType refresh when:
     * - when user want to refresh data
     * - when data in room outdated
     * if type refresh:
     * insert notes (replace if duplicate) with time to have last time cache
     * if append, prepend (user paging) will add simple note
     * and insert tasks of notes (if duplicate will replace tasks)
     * */
    @WorkerThread
    private fun insertToDb(loadType: LoadType, data: List<Note>): List<Note> {
        database.runInTransaction {
            when (loadType) {
                REFRESH -> {
                    /**
                     * with first time fetch data from api will save current time to be the
                     * next times find data by room will check data outdated will update note for current user
                     * */
                    local.insertNotes(*data.map { note ->
                        note.apply {
                            createAt = System.currentTimeMillis()
                            modifiedAt = System.currentTimeMillis()
                        }
                    }.toTypedArray())
                }
                APPEND, PREPEND -> {
                    local.insertNotes(*data.toTypedArray())
                }
            }.andThen {
                data.forEach { note ->
                    local.insertTasks(*note.tasks.toTypedArray())
                }
            }
        }
        return data
    }

    /**
     *
     * */
    private fun getRemoteKeyForLastItem(state: PagingState<Int, Note>): Int? =
        state.pages.last().nextKey

    /**
     *
     * */
    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Note>): Int =
        state.pages.first().prevKey ?: 1
}