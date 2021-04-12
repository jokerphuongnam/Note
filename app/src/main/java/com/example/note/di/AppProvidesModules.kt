package com.example.note.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import androidx.room.Room
import com.example.note.model.database.local.AppDatabase
import com.example.note.model.database.local.note.RoomNoteImpl
import com.example.note.model.database.local.user.RoomUserImpl
import com.example.note.model.database.network.NetworkConnectionInterceptor
import com.example.note.model.database.network.note.NoteRetrofitServiceImpl
import com.example.note.model.database.network.user.UserRetrofitServiceImpl
import com.example.note.utils.DataStoreConstrain.DATA_STORE_NAME
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
    //data store
    @Provides
    @Singleton
    fun providerDataStore(@ApplicationContext context: Context): RxDataStore<Preferences> =
        RxPreferenceDataStoreBuilder(context, DATA_STORE_NAME).build()

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
    fun providerGson(): Gson = GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create()

    @Provides
    @Singleton
    fun providerGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun providerRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory =
        RxJava3CallAdapterFactory.create()

    @Provides
    @Singleton
    fun providerOkHttp(interceptor: NetworkConnectionInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

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