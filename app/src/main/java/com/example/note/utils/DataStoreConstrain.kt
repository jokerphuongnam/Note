package com.example.note.utils

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey


object DataStoreConstrain {
    const val DATA_STORE_NAME = "settings_pref"
    val IS_DARK_MODE: Preferences.Key<Boolean> by lazy {
        booleanPreferencesKey("dark_mode")
    }
    val CURRENT_UID: Preferences.Key<Long> by lazy {
        longPreferencesKey("uid")
    }
}