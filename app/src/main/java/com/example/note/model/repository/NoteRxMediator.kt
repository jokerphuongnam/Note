package com.example.note.model.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.example.note.model.database.domain.Note
import com.example.note.model.database.local.AppDatabase
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.local.user.CurrentUser
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.utils.PagingUtil
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalPagingApi
class NoteRxMediator @Inject constructor(
    private val local: NoteLocal,
    private val network: NoteNetwork,
    private val currentCurrentUser: CurrentUser,
    private val database: AppDatabase
) : RxRemoteMediator<Int, Note>() {
    private var _maxCount: Long = 0
    var maxCount: Long
        get() =  _maxCount
        set(value) {_maxCount = value}
    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Note>
    ): Single<MediatorResult> = Single.just(loadType).observeOn(Schedulers.io()).map {
        when (loadType) {
            REFRESH -> {
                0
            }
            APPEND -> {
                getRemoteKeyForLastItem(state)
            }
            PREPEND -> {
                getRemoteKeyForFirstItem(state)
            }
            else -> {
                0
            }
        }
    }.flatMap { page ->
        currentCurrentUser.uid.map { uid ->
            uid to page
        }
    }.flatMap { uidToPage ->
        val (uid, page) = uidToPage
        val firstNote = state.firstItemOrNull()
        var type = loadType
        if (page == 0 || firstNote == null || System.currentTimeMillis() - firstNote.modifiedAt == 500L) {
            local.deleteNotes(uid!!)
            type = REFRESH
        }
        val currentPageNumber: Int = page ?: 1
        val (start, end) = PagingUtil.pageToItem(currentPageNumber)
        network.fetchNotes(uid!!, start, end).map {
            insertToDb(type, it.body() ?: emptyList())
        }.map<MediatorResult> {
            MediatorResult.Success(end.equals(maxCount - 1))
        }.onErrorReturn {
            MediatorResult.Error(it)
        }
    }.onErrorReturn { MediatorResult.Error(it) }

    @WorkerThread
    private fun insertToDb(loadType: LoadType, data: List<Note>): List<Note> {
        database.runInTransaction {
            when(loadType){
                REFRESH ->{
                    local.insertNotesWithTimestamp(*data.toTypedArray())
                }
                APPEND, PREPEND ->{
                    local.insertNotes(*data.toTypedArray())
                }
            }
            data.forEach { note ->
                local.insertTasks(*note.tasks.toTypedArray())
            }
        }
        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Note>): Int =
        state.pages.last().nextKey!!

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Note>): Int =
        state.pages.first().prevKey ?: 1
}