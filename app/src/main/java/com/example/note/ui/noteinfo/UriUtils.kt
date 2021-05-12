package com.example.note.ui.noteinfo

import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


fun List<Uri>.toMultipartBodies(
    partName: String,
    context: Context
): List<MultipartBody.Part> {
    return map {
        /**
         * map each element from Uri to MultipartBody.Part
         * */
        it.toMultipartBody(partName, context)
    }
}

fun Uri.toMultipartBody(partName: String, context: Context): MultipartBody.Part {
    /**
     * create File object
     * */
    val file = File(toString())
    /**
     * 3 parameter
     * name of file upload (depend on server)
     * name
     * conent
     * */
    return MultipartBody.Part.createFormData(
        partName,
        file.name,
        RequestBody.create(
            MediaType.parse(context.contentResolver.getType(this)),
            file
        )
    )
}