package com.example.note.utils

object PagingUtil {
    const val INIT_LOAD_SIZE = 15
    const val PAGE_SIZE = 10
    const val PREFECT_DISTANCE = 5
    const val OUT_DATE_TIME_STAMP = 120000000

    /**
     * if START = 15, LOOP = 10
     * if page == 1: start: end 0: 15 -1
     * if page == 2: start: end 15 + 10 * (page - 1 - page 1(results are available)): end 15 + 10 * (page - 1)
     * */
    fun pageToItem(page: Int): Pair<Long, Long> {
        return when (page) {
            1 -> {
                0L to (INIT_LOAD_SIZE - 1).toLong()
            }
            else -> {
                (INIT_LOAD_SIZE + PAGE_SIZE * (page - 2)).toLong() to (INIT_LOAD_SIZE + PAGE_SIZE * (page -1) - 1).toLong()
            }
        }
    }
}