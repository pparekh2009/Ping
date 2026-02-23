package com.priyanshparekh.ping.websocket

import com.google.gson.JsonElement

data class SocketEvent(
    val socketEventType: SocketEventType,
    val payload: JsonElement
)