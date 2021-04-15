package com.example.note.model.database.network.note

import com.example.note.model.database.domain.Note
import com.example.note.model.database.domain.Task
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.util.Date
import javax.inject.Inject

interface NoteRetrofitServiceImpl : NoteNetwork {

    override fun insertNote(
        uid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Note>> = insertNote(
        uid,
        note.title,
        note.isFavorite,
        note.detail,
        note.tags,
        note.tasks,
        images,
        sounds,
        note.noticeTimes?: emptyList()
    )

    override fun updateNote(
        uid: Long,
        nid: Long,
        note: Note,
        images: List<MultipartBody.Part>,
        sounds: List<MultipartBody.Part>
    ): Single<Response<Note>> = updateNote(
        uid,
        note.nid,
        note.title,
        note.isFavorite,
        note.detail,
        note.tags,
        note.tasks,
        images,
        note.images?: emptyList(),
        sounds,
        note.sounds?: emptyList(),
        note.noticeTimes?: emptyList()
    )

    @DELETE("delete/{uid}/{nid}")
    override fun deleteNote(uid: Long, nid: Long): Single<Response<Note>>

    @GET("notes")
    override fun fetchNotes(
        uid: Long,
        start: Long,
        amount: Long
    ): Single<Response<MutableList<Note>>>

    @GET("nodecount")
    override fun fetchCount(uid: Long): Single<Response<Long>>

    @Multipart
    @FormUrlEncoded
    @POST("insert")
    fun insertNote(
        @Field("uid") uid: Long,
        @Field("title") title: String,
        @Field("isFavorite") isFavorite: Boolean,
        @Field("detail")detail: String,
        @Field("tags") tags: List<String>,
        @Field("tasks")tasks: List<Task>,
        @Part("images") images: List<MultipartBody.Part>,
        @Part("sounds") sounds: List<MultipartBody.Part>,
        @Field("noticeTimes")noticeTimes: List<Long>
    ): Single<Response<Note>>

    @Multipart
    @FormUrlEncoded
    @PUT("update")
    fun updateNote(
        @Field("uid") uid: Long,
        @Field("nid")nid: Long,
        @Field("title")title: String,
        @Field("isFavorite")isFavorite: Boolean,
        @Field("detail")detail: String,
        @Field("tags")tags: List<String>,
        @Field("tasks")tasks: List<Task>,
        @Part("images") images: List<MultipartBody.Part>,
        @Field("images")imagesString: List<String>,
        @Part("sounds") sounds: List<MultipartBody.Part>,
        @Field("sounds")soundsString: List<String>,
        @Field("noticeTimes")noticeTimes: List<Long>
    ): Single<Response<Note>>
}