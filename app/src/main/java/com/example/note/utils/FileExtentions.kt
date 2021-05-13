package com.example.note.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream


fun List<Bitmap>.toMultipartBodies(
    partName: String
): List<MultipartBody.Part> {
    return map {
        /**
         * map each element from Uri to MultipartBody.Part
         * */
        it.toMultipartBody(partName)
    }
}

fun Bitmap.toMultipartBody(partName: String): MultipartBody.Part {
    /**
     * create File object
     * */
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    /**
     * 3 parameter
     * name of file upload (depend on server)
     * name
     * conent
     * */
    return MultipartBody.Part.createFormData(
        partName,
        "filename.jpeg",
        RequestBody.create(
            MediaType.parse("image/jpeg"),
            byteArray
        )
    )
}