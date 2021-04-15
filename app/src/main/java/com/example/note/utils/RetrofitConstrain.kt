package com.example.note.utils

object RetrofitConstrain {
    private const val PORT: Int = 3000
    private const val IP: String = "192.168.1.199"
    private const val DYNAMIC_IP: String = "192.168.43.108"
    private const val IP_URL = "$IP:$PORT"
    const val BASE_URL = "http://$IP_URL/"
    const val EMAIL_PASS = "email_pass"
    const val GOOGLE_SIGN_IN = "google_sign_in"

    const val NOT_FOUND = 404
    const val CONFLICT = 409
}