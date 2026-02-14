package com.priyanshparekh.ping.chatlist

import com.priyanshparekh.ping.chatlist.dto.GetChatResponse

data class ChatListUiState(
    var searchInput: String = "",
    val chatList: List<GetChatResponse> = listOf(),
    val errorMessage: String? = null
)