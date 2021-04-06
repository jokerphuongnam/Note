package com.example.note.utils

object RetrofitConstrain {
    private const val PORT: Int = 3000
    private const val IP: String = "192.168.1.199"
    private const val IP_URL = "$PORT:$IP"
    const val BASE_URL = "$IP_URL/"
}