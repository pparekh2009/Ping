package com.priyanshparekh.ping.chat

data class ChatUiState(
    val name: String = "",
    val messageInput: String = "",
    val chatMessageUis: List<ChatMessageUi> = emptyList(),
    val errorMessage: String? = null
)
