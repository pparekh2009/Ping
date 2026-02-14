package com.priyanshparekh.ping

import android.app.Application
import com.priyanshparekh.ping.util.DataStoreManager
import com.priyanshparekh.ping.util.authStore

class Ping: Application() {

    companion object {
        lateinit var dataStoreManager: DataStoreManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        dataStoreManager = DataStoreManager(authStore)
    }

}