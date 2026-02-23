package com.priyanshparekh.ping.chat

data class ChatMessageUi(
    val chatMessage: String,
    val isOutgoing: Boolean,
    val chatMessageStatus: ChatMessageStatus
)
