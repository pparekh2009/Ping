package com.priyanshparekh.ping.websocket.event

data class ChatMessagePayload(
    val messageId: Long,
    val chatId: Long,
    val senderId: Long,
    val message: String
)
