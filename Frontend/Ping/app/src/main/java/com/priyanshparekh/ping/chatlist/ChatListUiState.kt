package com.priyanshparekh.ping.chatlist

import com.priyanshparekh.ping.chatlist.dto.ChatResponse

data class ChatListUiState(
    var searchInput: String = "",
    val chatList: List<ChatResponse> = listOf(),
    val errorMessage: String? = null
)