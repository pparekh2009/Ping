package com.priyanshparekh.ping.chat

data class ChatMessageResponse(
    val message: String,
    val messageId: Long,
    val chatId: Long,
    val senderId: Long,
    val chatMessageStatus: ChatMessageStatus
)