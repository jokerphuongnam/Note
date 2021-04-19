package com.example.note.utils

import androidx.annotation.StringRes
import com.example.note.R
import com.example.note.throwable.NoErrorException

object InputUtils {

    @Throws(NoErrorException::class)
    @StringRes
    fun passwordRegex(password: String): Int = when {
        password.isEmpty() -> {
            R.string.password_empty
        }
        password.length < 6 -> {
            R.string.password_length
        }
        password.indexOf(' ') != -1 -> {
            R.string.password_only_alphabet
        }
        else -> {
            throw NoErrorException()
        }
    }

}