package com.priyanshparekh.ping.auth.signup.dto

data class UserSignUpRequest(
    val name: String,
    val email: String,
    val password: String
)
