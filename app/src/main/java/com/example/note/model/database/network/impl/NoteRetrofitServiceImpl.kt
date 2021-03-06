package com.example.note.model.database.network.impl

import android.graphics.Bitmap
import com.example.note.model.database.domain.Note
import com.example.note.model.database.network.NoteNetwork
import com.example.note.utils.RetrofitUtils.IMAGES
import com.example.note.utils.RetrofitUtils.SOUNDS
import com.example.note.utils.toJson
import com.example.note.utils.toMultipartBodies
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Inject


class NoteRetrofitServiceImpl @Inject constructor(
    private val service: Service
) : NoteNetwork {

    override fun insertNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>
    ): Single<Response<Note>> {
        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        return service.insertNote(
            RequestBody.create(MultipartBody.FORM, note.userId.toString()),
            RequestBody.create(MultipartBody.FORM, note.title),
            RequestBody.create(MultipartBody.FORM, note.isFavorite.toString()),
            RequestBody.create(MultipartBody.FORM, note.detail),
            RequestBody.create(MultipartBody.FORM, gson.toJson(note.tags)),
            RequestBody.create(MultipartBody.FORM, gson.toJson(note.tasks)),
            images.toMultipartBodies(IMAGES),
            sounds.toMultipartBodies(SOUNDS),
            RequestBody.create(MultipartBody.FORM, gson.toJson(note.noticeTimes))
        )
    }

    override fun updateNote(
        note: Note,
        images: List<Bitmap>,
        sounds: List<Bitmap>
    ): Single<Response<Note>> {
        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
        return service.updateNote(
            RequestBody.create(MultipartBody.FORM, note.userId.toString()),
            RequestBody.create(MultipartBody.FORM, note.nid.toString()),
            RequestBody.create(MultipartBody.FORM, note.title),
            RequestBody.create(MultipartBody.FORM, note.isFavorite.toString()),
            RequestBody.create(MultipartBody.FORM, note.detail),
            RequestBody.create(MultipartBody.FORM, gson.toJson(note.tags)),
            RequestBody.create(MultipartBody.FORM, gson.toJson(note.tasks)),
            images.toMultipartBodies(IMAGES),
            RequestBody.create(MultipartBody.FORM, note.images.toJson()),
            sounds.toMultipartBodies(SOUNDS),
            RequestBody.create(MultipartBody.FORM, note.sounds.toJson()),
            RequestBody.create(MultipartBody.FORM, gson.toJson(note.noticeTimes)),
        )
    }

    override fun deleteNote(uid: Long, nid: Long): Single<Response<Note>> =
        service.deleteNote(uid, nid)

    override fun fetchNotes(
        uid: Long,
        start: Long,
        amount: Long
    ): Single<Response<MutableList<Note>>> = service.fetchNotes(uid, start, amount)

    override fun fetchCount(uid: Long): Single<Response<Long>> = service.fetchCount(uid)

    interface Service {

        @DELETE("delete/{uid}/{nid}")
        fun deleteNote(@Path("uid") uid: Long,@Path("nid") nid: Long): Single<Response<Note>>

        @GET("notes/{uid}")
        fun fetchNotes(
            @Path("uid") uid: Long,
            @Query("start") start: Long,
            @Query("amount") amount: Long
        ): Single<Response<MutableList<Note>>>

        @GET("nodecount")
        fun fetchCount(@Query("uid") uid: Long): Single<Response<Long>>

        @Multipart
        @POST("insert")
        fun insertNote(
            @Part("uid") uid: RequestBody,
            @Part("title") title: RequestBody,
            @Part("isFavorite") isFavorite: RequestBody,
            @Part("detail") detail: RequestBody,
            @Part("tags") tags: RequestBody,
            @Part("tasks") tasks: RequestBody,
            @Part images: List<MultipartBody.Part>,
            @Part sounds: List<MultipartBody.Part>,
            @Part("noticeTimes") noticeTimes: RequestBody
        ): Single<Response<Note>>


        @Multipart
        @PUT("update")
        fun updateNote(
            @Part("uid") uid: RequestBody,
            @Part("nid") nid: RequestBody,
            @Part("title") title: RequestBody,
            @Part("isFavorite") isFavorite: RequestBody,
            @Part("detail") detail: RequestBody,
            @Part("tags") tags: RequestBody,
            @Part("tasks") tasks: RequestBody,
            @Part images: List<MultipartBody.Part>,
            @Part("images") imagesString: RequestBody,
            @Part sounds: List<MultipartBody.Part>,
            @Part("sounds") soundsString: RequestBody,
            @Part("noticeTimes") noticeTimes: RequestBody
        ): Single<Response<Note>>
    }
}