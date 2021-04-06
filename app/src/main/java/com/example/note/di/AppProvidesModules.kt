package com.example.note.di

import android.content.Context
import androidx.room.Room
import com.example.note.model.database.local.AppDatabase
import com.example.note.model.database.local.note.NoteLocal
import com.example.note.model.database.local.note.RoomNoteImpl
import com.example.note.model.database.local.user.RoomUserImpl
import com.example.note.model.database.local.user.UserLocal
import com.example.note.model.database.network.note.NoteNetwork
import com.example.note.model.database.network.note.NoteRetrofitServiceImpl
import com.example.note.model.database.network.user.UserNetwork
import com.example.note.model.database.network.user.UserRetrofitServiceImpl
import com.example.note.utils.RetrofitConstrain.BASE_URL
import com.example.note.utils.RoomConstrain.DB_NAME
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModules {
    //room
    @Provides
    @Singleton
    fun providerRoomDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()

    @Provides
    @Singleton
    fun providerNoteLocal(appDatabase: AppDatabase): RoomNoteImpl.NoteDao = appDatabase.noteDao()

    @Provides
    @Singleton
    fun providerUserLocal(appDatabase: AppDatabase): RoomUserImpl.UserDao = appDatabase.userDao()

    //retrofit
    @Provides
    @Singleton
    fun providerGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().setDateFormat("HH:mm:ss dd-MM-yyyy").create())

    @Provides
    @Singleton
    fun providerRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory =
        RxJava3CallAdapterFactory.create()

    @Provides
    @Singleton
    fun providerOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providerRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        okHttp: OkHttpClient
    ): Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(rxJava3CallAdapterFactory)
        .client(okHttp).build()

    @Provides
    @Singleton
    fun providerNoteService(retrofit: Retrofit): NoteRetrofitServiceImpl.Service =
        retrofit.create(NoteRetrofitServiceImpl.Service::class.java)

    @Provides
    @Singleton
    fun providerUserService(retrofit: Retrofit): UserRetrofitServiceImpl.Service =
        retrofit.create(UserRetrofitServiceImpl.Service::class.java)
}