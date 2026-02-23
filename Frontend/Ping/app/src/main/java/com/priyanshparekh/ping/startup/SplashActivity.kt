package com.priyanshparekh.ping.startup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.priyanshparekh.ping.auth.AuthViewModel
import com.priyanshparekh.ping.auth.login.LoginActivity
import com.priyanshparekh.ping.chatlist.ChatListActivity
import com.priyanshparekh.ping.network.WebSocketManager
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private val tag = "Splash_Activity"

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            Log.d(tag, "onCreate: setKeepOnScreenCondition: isChecking: ${authViewModel.authStatus.value.isChecking}")
            authViewModel.authStatus.value.isChecking
        }

        authViewModel.checkAuth()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authStatus.collect { authStatus ->
                    if (!authStatus.isChecking) {
                        if (authStatus.isLoggedIn) {
                            if (!WebSocketManager.isConnected()) {
                                WebSocketManager.connect()
                            }
                            startActivity(Intent(this@SplashActivity, ChatListActivity::class.java))
                        } else {
                            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        }
                        finish()
                    }
                }
            }
        }
    }
}