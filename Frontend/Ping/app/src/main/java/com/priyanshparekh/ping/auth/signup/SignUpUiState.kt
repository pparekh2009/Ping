package com.priyanshparekh.ping.auth.signup

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val errorMessage: String? = null
)