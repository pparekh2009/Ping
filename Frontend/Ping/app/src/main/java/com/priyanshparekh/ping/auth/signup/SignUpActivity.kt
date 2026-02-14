package com.priyanshparekh.ping.auth.signup

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
import com.priyanshparekh.ping.auth.login.LoginActivity
import com.priyanshparekh.ping.chatlist.ChatListActivity
import com.priyanshparekh.ping.network.WebSocketManager

class SignUpActivity : ComponentActivity() {

    val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val uiState = viewModel.signUpUiState.collectAsState()

            if (uiState.value.isSignUpSuccessful) {
                LaunchedEffect(Unit) {
                    WebSocketManager.connect()

                    Toast.makeText(this@SignUpActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignUpActivity, ChatListActivity::class.java))
                    finish()
                }
            }

            SignUpScreen(
                signUpUiState = uiState.value,
                onNameChange = viewModel::updateSignUpName,
                onEmailChange = viewModel::updateSignUpEmail,
                onPasswordChange = viewModel::updateSignUpPassword,
                onSignUpClick = viewModel::signUp,
                onToLoginClick = {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                })
        }
    }
}