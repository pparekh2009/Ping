package com.priyanshparekh.ping.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshparekh.ping.auth.login.LoginUiState
import com.priyanshparekh.ping.auth.login.dto.UserLoginRequest
import com.priyanshparekh.ping.auth.signup.SignUpUiState
import com.priyanshparekh.ping.auth.signup.dto.UserSignUpRequest
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.startup.AuthStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val tag = "Auth_View_Model"

    private val authRepository = AuthRepository()

    private val _authStatus: MutableStateFlow<AuthStatus> = MutableStateFlow(AuthStatus())
    val authStatus: StateFlow<AuthStatus> = _authStatus

    fun checkAuth() {
        viewModelScope.launch {
            val token = authRepository.getAccessToken().first()
            Log.d(tag, "checkAuth: token: $token")
            _authStatus.update {
                it.copy(
                    isChecking = false,
                    isLoggedIn = token != null
                )
            }
        }
    }


    private val _signUpUiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState

    fun updateSignUpName(name: String) {
        _signUpUiState.update {
            it.copy(name = name)
        }
    }

    fun updateSignUpEmail(email: String) {
        _signUpUiState.update {
            it.copy(email = email)
        }
    }

    fun updateSignUpPassword(password: String) {
        _signUpUiState.update {
            it.copy(password = password)
        }
    }

    fun signUp() {

        if (_signUpUiState.value.isLoading) return

        viewModelScope.launch {
            val currentUiState = _signUpUiState.value
            val userSignUpRequest = UserSignUpRequest(
                name = currentUiState.name,
                email = currentUiState.email,
                password = currentUiState.password
            )

            if (userSignUpRequest.name.isBlank() ||
                userSignUpRequest.email.isBlank() ||
                userSignUpRequest.password.isBlank()) {
                _signUpUiState.update {
                    it.copy(isLoading = false, isSignUpSuccessful = false, errorMessage = "All fields are required!")
                }
                return@launch
            }



            _signUpUiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }


            val signUpResponse = authRepository.signUp(userSignUpRequest)

            when (signUpResponse) {
                is ApiResponse.SUCCESS -> {
                    _signUpUiState.update {
                        it.copy(isLoading = false, isSignUpSuccessful = true, errorMessage = null)
                    }
                }
                is ApiResponse.ERROR -> {
                    _signUpUiState.update {
                        it.copy(isLoading = false, isSignUpSuccessful = false, errorMessage = signUpResponse.message)
                    }
                }
            }
        }
    }




    private val _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    fun updateLoginEmail(email: String) {
        _loginUiState.update {
            it.copy(email = email)
        }
    }

    fun updateLoginPassword(password: String) {
        _loginUiState.update {
            it.copy(password = password)
        }
    }

    fun login() {
        Log.d(tag, "login: email: ${_loginUiState.value.email}")
        Log.d(tag, "login: password: ${_loginUiState.value.password}")

        if (_loginUiState.value.isLoading) return

        viewModelScope.launch {

            val currentUiState = _loginUiState.value
            val userLoginRequest = UserLoginRequest(
                email = currentUiState.email,
                password = currentUiState.password
            )


            if (userLoginRequest.email.isBlank() ||
                userLoginRequest.password.isBlank()) {
                _loginUiState.update {
                    it.copy(isLoading = false, isLoginSuccessful = false, errorMessage = "All fields are required!")
                }
                return@launch
            }



            _loginUiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }


            val loginResponse = authRepository.login(userLoginRequest)

            when (loginResponse) {
                is ApiResponse.SUCCESS -> {
                    _loginUiState.update {
                        it.copy(isLoading = false, isLoginSuccessful = true, errorMessage = null)
                    }
                }
                is ApiResponse.ERROR -> {
                    Log.d(tag, "login: message: ${loginResponse.message}")
                    _loginUiState.update {
                        it.copy(isLoading = false, isLoginSuccessful = false, errorMessage = loginResponse.message)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}