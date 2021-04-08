package com.example.note.model.repository

import androidx.annotation.WorkerThread
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.note.model.database.domain.Note
import com.example.note.model.database.local.user.CurrentUser
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.utils.PagingUtil
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRxPagingSource @Inject constructor(
    private val network: NoteNetwork,
    private val currentCurrentUser: CurrentUser
) :
    RxPagingSource<Int, Note>() {

    @WorkerThread
    override fun getRefreshKey(state: PagingState<Int, Note>): Int? {
        val anchorPosition: Int = state.anchorPosition ?: return null
        val (_, prevKey, nextKey) = state.closestPageToPosition(anchorPosition) ?: return null
        if (prevKey != null) {
            return prevKey + STARTING_PAGE_INDEX
        }
        return if (nextKey != null) {
            nextKey - 1
        } else {
            null
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Note>> {
        val currentPageNumber: Int = params.key ?: 1
        val (start, end) = PagingUtil.pageToItem(currentPageNumber)
        return currentCurrentUser.uid.flatMap { uid ->
            network.fetchNotes(uid!!, start, end).map {  response ->
                toLoadResult(response.body() ?: mutableListOf(), currentPageNumber)
            }.onErrorReturn {
                LoadResult.Error(it)
            }
        }
    }

    @WorkerThread
    private fun toLoadResult(
        note: MutableList<Note>,
        page: Int
    ): LoadResult<Int, Note> {
        return LoadResult.Page(
            note,
            prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
            nextKey = page + 1
        )
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}