package com.example.note.utils

object RetrofitUtils {
    private const val PORT: Int = 3000
    private const val STATIC_IP: String = "192.168.1.199"
    private const val DYNAMIC_IP: String = "192.168.42.197"
    private const val LOCALHOST_IP: String = "10.0.2.2"
    private const val IP_URL: String = "$DYNAMIC_IP:$PORT"
    const val BASE_URL: String = "http://$IP_URL/"
    const val EMAIL_PASS: String = "email_pass"
    const val GOOGLE_SIGN_IN: String = "google_sign_in"

    const val NOT_FOUND: Int = 404
    const val CONFLICT: Int = 409
    const val INTERNAL_SERVER_ERROR: Int = 500

    fun getImageUrl(imageName: String) = "${BASE_URL}image/$imageName"
    fun getSoundUrl(soundName: String) = "${BASE_URL}sound/$soundName"

    const val IMAGES: String = "images"
    const val SOUNDS: String = "sounds"
    const val AVATAR: String = "avatar"
}