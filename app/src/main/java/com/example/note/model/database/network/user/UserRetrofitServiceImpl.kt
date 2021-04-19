package com.example.note.model.database.network.user

import com.example.note.model.database.domain.User
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import javax.inject.Inject

class UserRetrofitServiceImpl @Inject constructor(private val service: Service) : UserNetwork {

    override fun login(
        username: String,
        password: String,
        type: String
    ): Single<Response<User>> = service.login(username, password, type)

    override fun register(
        user: User,
        password: String,
        type: String,
        avatar: MultipartBody.Part?
    ): Single<Response<User>> = with(service) {
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
            if (avatar != null) {
                register(
                    RequestBody.create(MultipartBody.FORM,user.username),
                    RequestBody.create(MultipartBody.FORM,password),
                    RequestBody.create(MultipartBody.FORM,type),
                    avatar,
                    RequestBody.create(MultipartBody.FORM,user.fname),
                    RequestBody.create(MultipartBody.FORM,user.lname),
                    RequestBody.create(MultipartBody.FORM,user.birthDay.toString())
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
    }

    override fun editProfile(user: User, avatar: MultipartBody.Part?): Single<Response<User>> =
        with(service) {
            if (user.avatar != null) {
                editProfile(
                    user.uid,
                    user.avatar!!,
                    user.fname,
                    user.lname,
                    user.birthDay
                )
            } else {
                if (avatar != null) {
                    editProfile(
                        RequestBody.create(MultipartBody.FORM, user.uid.toString()),
                        avatar,
                        RequestBody.create(MultipartBody.FORM, user.fname),
                        RequestBody.create(MultipartBody.FORM, user.lname),
                        RequestBody.create(MultipartBody.FORM, user.birthDay.toString())
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
        }

    override fun changePassword(
        username: String,
        oldPassword: String,
        newPassword: String
    ): Single<Response<User>> = service.changePassword(username, oldPassword, newPassword)

    override fun forgotPassword(username: String): Single<Response<Int>> =
        service.forgotPassword(username)

    interface Service {
        @GET("login")
        fun login(
            @Query("username") username: String,
            @Query("password") password: String,
            @Query("type") type: String
        ): Single<Response<User>>

        @FormUrlEncoded
        @PUT("changepassword")
        fun changePassword(
            @Field("username") username: String,
            @Field("old_password") oldPassword: String,
            @Field("new_password") newPassword: String
        ): Single<Response<User>>

        @FormUrlEncoded
        @PUT("forgotpassword")
        fun forgotPassword(@Field("username") username: String): Single<Response<Int>>

        @Multipart
        @POST("register")
        fun register(
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("type") type: RequestBody,
            @Part avatar: MultipartBody.Part,
            @Part("fname") firstName: RequestBody,
            @Part("lname") lastName: RequestBody,
            @Part("birthDay") birthDay: RequestBody
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
        @PUT("editprofile")
        fun editProfile(
            @Field("uid") uid: RequestBody,
            @Part avatar: MultipartBody.Part,
            @Field("fname") firstName: RequestBody,
            @Field("lname") lastName: RequestBody,
            @Field("birthDay") birthDay: RequestBody
        ): Single<Response<User>>

        @FormUrlEncoded
        @PUT("editprofile")
        fun editProfile(
            @Field("uid") uid: Long,
            @Field("avatar") avatar: String,
            @Field("fname") firstName: String,
            @Field("lname") lastName: String,
            @Field("birthDay") birthDay: Long
        ): Single<Response<User>>

        @FormUrlEncoded
        @PUT("editprofile")
        fun editProfile(
            @Field("uid") uid: Long,
            @Field("fname") firstName: String,
            @Field("lname") lastName: String,
            @Field("birthDay") birthDay: Long
        ): Single<Response<User>>
    }
}