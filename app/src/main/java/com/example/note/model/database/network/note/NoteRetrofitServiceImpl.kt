package com.example.note.model.database.network.note

import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface NoteRetrofitServiceImpl : NoteNetwork {

    override fun insertNote(
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Note>> = insertNote(
        MultipartBody.Builder().apply {
            val gson = Gson()
            setType(MultipartBody.FORM)
            addFormDataPart("uid", note.nid.toString())
            addFormDataPart("title", note.title)
            addFormDataPart("isFavorite", if (note.isFavorite) "t" else "f")
            addFormDataPart("detail", note.detail)
            addFormDataPart("tags", gson.toJson(note.tags))
            addFormDataPart("tasks", gson.toJson(note.tasks))
            addFormDataPart("noticeTimes", gson.toJson(note.noticeTimes))
        }.build(),
        images,
        sounds
    )

    override fun updateNote(
        nid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Note>> = updateNote(
        note.userId!!,
        note.nid,
        note.title,
        note.isFavorite,
        note.detail,
        note.tags,
        note.tasks,
        images,
        note.images ?: emptyList(),
        sounds,
        note.sounds ?: emptyList(),
        note.noticeTimes ?: emptyList()
    )

    @DELETE("delete/{uid}/{nid}")
    override fun deleteNote(uid: Long, nid: Long): Single<Response<Note>>

    @GET("notes")
    override fun fetchNotes(
        @Query("uid") uid: Long,
        @Query("start") start: Long,
        @Query("amount") amount: Long
    ): Single<Response<MutableList<Note>>>

    @GET("nodecount")
    override fun fetchCount(@Query("uid") uid: Long): Single<Response<Long>>

    @Multipart
    @POST("insert")
    fun insertNote(
        @Field("uid") uid: Long,
        @Field("title") title: String,
        @Field("isFavorite") isFavorite: Boolean,
        @Field("detail") detail: String,
        @Field("tags") tags: List<String>,
        @Field("tasks") tasks: List<Task>,
        @Part("images") images: List<MultipartBody.Part>,
        @Part("sounds") sounds: List<MultipartBody.Part>,
        @Field("noticeTimes") noticeTimes: List<Long>
    ): Single<Response<Note>>

    @Multipart
    @POST("insert")
    fun insertNote(
        @Body body: RequestBody,
        @Part("images") images: List<MultipartBody.Part>,
        @Part("sounds") sounds: List<MultipartBody.Part>,
    ): Single<Response<Note>>


    @Multipart
    @PUT("update")
    fun updateNote(
        @Field("uid") uid: Long,
        @Field("nid") nid: Long,
        @Field("title") title: String,
        @Field("isFavorite") isFavorite: Boolean,
        @Field("detail") detail: String,
        @Field("tags") tags: List<String>,
        @Field("tasks") tasks: List<Task>,
        @Part("images") images: List<MultipartBody.Part>,
        @Field("images") imagesString: List<String>,
        @Part("sounds") sounds: List<MultipartBody.Part>,
        @Field("sounds") soundsString: List<String>,
        @Field("noticeTimes") noticeTimes: List<Long>
    ): Single<Response<Note>>
}