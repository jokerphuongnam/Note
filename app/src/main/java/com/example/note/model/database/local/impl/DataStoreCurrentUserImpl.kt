package com.example.note.model.database.local.impl

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import com.example.note.model.database.local.CurrentUser
import com.example.note.utils.DataStoreConstrain
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class DataStoreCurrentUserImpl @Inject constructor(private val dataStore: RxDataStore<Preferences>) :
    CurrentUser {

    /**
     * get id with Flowable
     * get first if first time is complete will return error
     * */
    override val uid: Flowable<Long?> by lazy {
        dataStore.data().map { prefsIn ->
            prefsIn[DataStoreConstrain.CURRENT_UID]
        }
    }

    /**
     * after get sign in will change current id of user in data store
     * */
    override fun changeCurrentUser(uid: Long): Single<Preferences> =
        dataStore.updateDataAsync { prefsIn ->
            val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
            mutablePreferences[DataStoreConstrain.CURRENT_UID] = uid
            Single.just(mutablePreferences)
        }

    /**
     * sign out will set null for current id of user in data store
     * */
    override fun signOut(): Single<Preferences> = dataStore.updateDataAsync { prefsIn ->
        val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
        mutablePreferences.remove(DataStoreConstrain.CURRENT_UID)
        Single.just(mutablePreferences)
    }
}