package com.priyanshparekh.ping.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.priyanshparekh.ping.auth.AuthViewModel
import com.priyanshparekh.ping.auth.signup.SignUpActivity
import com.priyanshparekh.ping.chatlist.ChatListActivity
import com.priyanshparekh.ping.network.WebSocketManager

class LoginActivity : ComponentActivity() {

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState = authViewModel.loginUiState.collectAsState()

            if (uiState.value.isLoginSuccessful) {
                LaunchedEffect(Unit) {
                    WebSocketManager.connect()

                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, ChatListActivity::class.java))
                    finish()
                }
            }

            LoginScreen(
                loginUiState = uiState.value,
                onEmailChange = authViewModel::updateLoginEmail,
                onPasswordChange = authViewModel::updateLoginPassword,
                onLoginClick = authViewModel::login,
                onToSignUpClick = {
                    startActivity(Intent(this, SignUpActivity::class.java))
                    finish()
                }
            )
        }
    }
}


