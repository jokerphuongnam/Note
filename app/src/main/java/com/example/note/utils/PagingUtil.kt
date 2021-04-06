package com.example.note.utils

object PagingUtil {
    private const val START = 4
    private const val LOOP = 2
    fun pageToItem(page: Int): Pair<Int, Int> {
        return when (page) {
            1 -> {
                0 to 4
            }
            else -> {
                4 + 2 * (page - 1) to 4 + 2 * page
            }
        }
    }
}