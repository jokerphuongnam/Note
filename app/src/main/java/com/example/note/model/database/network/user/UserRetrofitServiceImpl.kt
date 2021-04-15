package com.example.note.model.database.network.user

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserRetrofitServiceImpl : UserNetwork {

    @GET("login")
    override fun login(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("type") type: String
    ): Single<Response<User>>

    override fun register(
        user: User,
        password: String,
        type: String,
        avatar: MultipartBody.Part?
    ): Single<Response<User>> = if (avatar != null) {
        register(
            user.username,
            password,
            type,
            avatar,
            user.fname,
            user.lname,
            user.birthDay
        )
    } else {
        if (user.avatar != null) {
            register(
                user.username,
                password,
                type,
                user.avatar!!,
                user.fname,
                user.lname,
                user.birthDay
            )
        } else {
            register(
                user.username,
                password,
                type,
                user.fname,
                user.lname,
                user.birthDay
            )
        }
    }

    override fun editProfile(user: User, avatar: MultipartBody.Part?): Single<Response<User>> =
        if (avatar != null) {
            editProfile(
                user.uid,
                avatar,
                user.fname,
                user.lname,
                user.birthDay
            )
        } else {
            if (user.avatar != null) {
                editProfile(
                    user.uid,
                    user.avatar!!,
                    user.fname,
                    user.lname,
                    user.birthDay
                )
            } else {
                editProfile(
                    user.uid,
                    user.fname,
                    user.lname,
                    user.birthDay
                )
            }
        }


    @FormUrlEncoded
    @PUT("changepassword")
    override fun changePassword(
        @Field("username") username: String,
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String
    ): Single<Response<User>>

    @FormUrlEncoded
    @PUT("forgotpassword")
    override fun forgotPassword(@Field("username") username: String): Single<Response<User>>

    @Multipart
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("type") type: String,
        @Part("avatar") avatar: MultipartBody.Part,
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("birthDay") birthDay: Long
    ): Single<Response<User>>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("type") type: String,
        @Field("avatar") avatar: String,
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("birthDay") birthDay: Long
    ): Single<Response<User>>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("type") type: String,
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("birthDay") birthDay: Long
    ): Single<Response<User>>

    @Multipart
    @FormUrlEncoded
    @PUT("register")
    fun editProfile(
        @Field("uid") uid: Long,
        @Part("avatar") avatar: MultipartBody.Part,
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("birthDay") birthDay: Long
    ): Single<Response<User>>

    @FormUrlEncoded
    @PUT("register")
    fun editProfile(
        @Field("uid") uid: Long,
        @Field("avatar") avatar: String,
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("birthDay") birthDay: Long
    ): Single<Response<User>>

    @FormUrlEncoded
    @PUT("register")
    fun editProfile(
        @Field("uid") uid: Long,
        @Field("fname") firstName: String,
        @Field("lname") lastName: String,
        @Field("birthDay") birthDay: Long
    ): Single<Response<User>>
}