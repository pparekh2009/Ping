package com.priyanshparekh.ping.chat

data class MessagePayload(
    val messageType: MessageType,
    val senderId: Long,
    val chatId: Long,
    val payload: String
)
