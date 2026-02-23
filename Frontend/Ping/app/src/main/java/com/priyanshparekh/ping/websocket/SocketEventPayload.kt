package com.priyanshparekh.ping.websocket

sealed class SocketEventPayload {

    data class ChatMessagePayload(
        val messageId: Long,
        val chatId: Long,
        val senderId: Long,
        val message: String
    ): SocketEventPayload()

//    data class ChatStatusPayload(
//        val messageId:
//    )
}