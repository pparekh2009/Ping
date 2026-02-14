package com.priyanshparekh.ping.auth.login.dto

data class UserLoginResponse(
    val userId: Long,
    val accessToken: String
)
