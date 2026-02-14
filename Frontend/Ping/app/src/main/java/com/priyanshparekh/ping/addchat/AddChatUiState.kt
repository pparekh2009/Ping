package com.priyanshparekh.ping.addchat

import com.priyanshparekh.ping.user.User

data class AddChatUiState(
    val query: String = "",
    val searchResult: List<User> = listOf(),
    val errorMessage: String? = null
)
