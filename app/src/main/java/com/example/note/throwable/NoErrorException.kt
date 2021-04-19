package com.example.note.throwable

import java.io.IOException

class NoErrorException(private val _message: String? = null) : IOException(_message) {
    override val message: String
        get() = _message ?: "No Input Error"
}