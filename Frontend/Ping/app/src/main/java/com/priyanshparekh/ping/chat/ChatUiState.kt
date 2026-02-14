package com.priyanshparekh.ping.chat

data class ChatUiState(
    val name: String = "",
    val messageInput: String = "",
    val messages: List<Message> = emptyList(),
    val errorMessage: String? = null
)
