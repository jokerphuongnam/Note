package com.example.note.utils

object PagingUtil {
    const val START = 15
    const val LOOP = 10
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
                0L to (START - 1).toLong()
            }
            else -> {
                (START + LOOP * (page - 2)).toLong() to (START + LOOP * (page -1) - 1).toLong()
            }
        }
    }
}