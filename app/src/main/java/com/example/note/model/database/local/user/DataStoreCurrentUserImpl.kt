package com.example.note.model.database.local.user

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import com.example.note.utils.DataStoreUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class DataStoreCurrentUserImpl @Inject constructor(private val dataStore: RxDataStore<Preferences>) :
    CurrentUser {

    override val uid: Single<Long?>
        get() {
            return dataStore.data().map { ref ->
                ref[DataStoreUtil.CURRENT_UID]
            }.firstOrError()
        }

    override fun changeCurrentUser(uid: Long): Completable = dataStore.updateDataAsync { prefsIn ->
        val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
        mutablePreferences[DataStoreUtil.CURRENT_UID] = uid
        Single.just(prefsIn)
    }.flatMapCompletable {
        Completable.complete()
    }

    override fun signOut(): Completable = dataStore.updateDataAsync { prefsIn ->
        val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
        mutablePreferences.remove(DataStoreUtil.CURRENT_UID)
        Single.just(prefsIn)
    }.flatMapCompletable {
        Completable.complete()
    }
}