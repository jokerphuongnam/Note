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

    /**
     * check time if outdated time refresh else skip init refresh
     * */
    override fun initializeSingle(): Single<InitializeAction> =
        local.findLastUpdateSingle(_uid).map { firstNote ->
            if (System.currentTimeMillis() - firstNote.modifiedAt >= OUT_DATE_TIME_STAMP) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        }.onErrorReturn {
            InitializeAction.LAUNCH_INITIAL_REFRESH
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
                PagingUtil.UNKNOWN_PAGE
            }
            APPEND -> {
                getRemoteKeyForLastItem(state)
            }
            PREPEND -> {
                PagingUtil.PREPEND
            }
        }
    }.flatMap { page ->
        val firstNote = state.firstItemOrNull()

        /**
         * - page == null: user want to refresh notes
         * - firstNote == null: note not cached yet
         * - System.currentTimeMillis() - firstNote.modifiedAt: data outdated
         * will delete all data of user in cache and set type = refresh to save extend time
         * */
        val type = if (
            page == PagingUtil.UNKNOWN_PAGE ||
            firstNote == null ||
            System.currentTimeMillis() - firstNote.modifiedAt >= OUT_DATE_TIME_STAMP
        ) {
            local.clearNotesByUserId(_uid)
            REFRESH
        } else {
            loadType
        }
        if (page == PagingUtil.PREPEND) {
            /**
             * can't need prepend
             * */
            Single.just<MediatorResult>(MediatorResult.Success(false))
        } else {
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
            }
        }
    }.onErrorReturn {
        MediatorResult.Error(it)
    }

    /**
     * check loadType refresh when:
     * - when user want to refresh data
     * - when data in room outdated
     * if type refresh:
     * insert notes (replace if duplicate) with time to have last time cache
     * if append (user paging) will add simple note
     * if prepend do not think
     * and insert tasks of notes (if duplicate will replace tasks)
     * */
    @WorkerThread
    private fun insertToDb(loadType: LoadType, data: List<Note>): List<Note> {
        data.map { note ->
            note.userId = _uid
        }
        database.runInTransaction {
            when (loadType) {
                REFRESH -> {
                    /**
                     * with first time fetch data from api will save current time to be the
                     * next times find data by room will check data outdated will update note for current user
                     * */
                    local.insertNotesWithTime(data)
                }
                APPEND -> {
                    local.insertNotes(data)
                }
                PREPEND -> {
                    /**
                     * can't need prepend
                     * */
                }
            }
            data.forEach { note ->
                note.tasks.map { task ->
                    task.noteId = note.nid
                }
                local.insertTasks(*note.tasks.toTypedArray())
            }
        }
        return data
    }

    /**
     * get last key
     * */
    private fun getRemoteKeyForLastItem(state: PagingState<Int, Note>): Int? =
        state.pages.last().nextKey

    /**
     * get first key
     * */
    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Note>): Int =
        state.pages.first().prevKey ?: 1
}