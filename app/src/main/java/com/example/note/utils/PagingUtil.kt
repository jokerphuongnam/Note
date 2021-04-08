package com.example.note.utils

object PagingUtil {
    private const val START = 4
    private const val LOOP = 2
    fun pageToItem(page: Int): Pair<Int, Int> {
        return when (page) {
            1 -> {
                0 to START - 1
            }
            else -> {
                START + LOOP * (page - 2) to START + LOOP * (page -1) - 1
            }
        }
    }
}