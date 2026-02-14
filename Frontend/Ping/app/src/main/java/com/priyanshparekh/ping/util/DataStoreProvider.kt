package com.priyanshparekh.ping.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.authStore by preferencesDataStore(name = "auth_prefs")


