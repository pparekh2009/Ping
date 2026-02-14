package com.priyanshparekh.ping.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(
    private val authStore: DataStore<Preferences>
) {

    private val accessTokenKey = stringPreferencesKey("KEY_ACCESS_TOKEN")
    private val userIdTokenKey = longPreferencesKey("KEY_LONG_ID")

    suspend fun setAccessToken(accessToken: String) {
        authStore.edit { prefs ->
            prefs[accessTokenKey] = accessToken
        }
    }

    suspend fun clearAccessToken() {
        authStore.edit { prefs ->
            prefs.remove(accessTokenKey)
        }
    }

    fun getAccessToken() : Flow<String?> = authStore.data.map { prefs ->
        prefs[accessTokenKey]
    }

    suspend fun setUserId(userId: Long) {
        authStore.edit { prefs ->
            prefs[userIdTokenKey] = userId
        }
    }

    fun getUserId(): Flow<Long?> = authStore.data.map { prefs ->
        prefs[userIdTokenKey]
    }

}