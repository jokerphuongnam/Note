package com.example.note.model.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxRemoteMediator
import com.example.note.model.database.domain.Note
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
    private val currentCurrentUser: CurrentUser
) : RxRemoteMediator<Int, Note>() {
    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Note>
    ): Single<MediatorResult> = Single.just(loadType).observeOn(Schedulers.io()).map {
        when (it) {
            LoadType.REFRESH -> {
                0
            }
            LoadType.APPEND -> {
                getRemoteKeyForLastItem(state)
            }
            LoadType.PREPEND -> {
                getRemoteKeyForFirstItem(state)
            }
            else -> {
                0
            }
        }
    }.flatMap { page->
        if(page == 0){
            currentCurrentUser.uid.map {uid ->
                local.deleteNotes(uid!!)
            }
        }
        val currentPageNumber: Int = page ?: 1
        val (start, end) = PagingUtil.pageToItem(currentPageNumber)
        currentCurrentUser.uid.flatMap { uid ->
            network.fetchCount(uid!!).map <Long>{count->
                network.fetchNotes(uid, start, end).map {
                    insertToDb(page, loadType, it.body()?: emptyList())
                }
                count.body()
            }
        }.map {count ->
            MediatorResult.Success(!count.equals(end - 1))
        }
    }

    private fun insertToDb(page: Int, loadType: LoadType, data: List<Note>): List<Note> {

        return data
    }


    @WorkerThread
    private fun toLoadResult(
        note: MutableList<Note>,
        page: Int
    ): PagingSource.LoadResult<Int, Note> {
        return PagingSource.LoadResult.Page(
            note,
            prevKey = if (page == NoteRxPagingSource.STARTING_PAGE_INDEX) null else page - 1,
            nextKey = page + 1
        )
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Note>): Int = state.pages.last().nextKey!!

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Note>): Int = state.pages.first().prevKey ?: 1
}