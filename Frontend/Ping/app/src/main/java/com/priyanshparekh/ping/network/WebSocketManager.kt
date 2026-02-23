package com.priyanshparekh.ping.network

import android.util.Log
import com.priyanshparekh.ping.Ping.Companion.dataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object WebSocketManager {

    private val tag = "Web_Socket_Manager"

    private var webSocket: WebSocket? = null
    private var isConnected: Boolean = false

    private val _event: MutableSharedFlow<String> = MutableSharedFlow()
    val event: SharedFlow<String> = _event.asSharedFlow()

    private val socketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(tag, "socketListener: onOpen:")
            isConnected = true
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(tag, "socketListener: onMessage: text message: $text")
            CoroutineScope(Dispatchers.IO).launch {
                _event.emit(text)
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(tag, "socketListener: onMessage: bytes message: $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(tag, "socketListener: onClosing: code: $code")
            Log.d(tag, "socketListener: onClosing: reason: $reason")
            isConnected = false
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(tag, "socketListener: onClosed: code: $code")
            Log.d(tag, "socketListener: onClosed: reason: $reason")
            isConnected = false
        }

        override fun onFailure(
            webSocket: WebSocket,
            t: Throwable,
            response: Response?
        ) {
            Log.d(tag, "socketListener: onFailure: throwable: ${t.message}")
            isConnected = false
        }
    }

    fun connect() {
        if (webSocket != null) return

        val client = OkHttpClient()

        val token: String = runBlocking {
            dataStoreManager.getAccessToken().first() ?: ""
        }
        Log.d(tag, "webSocketManager: connect: token: $token")

        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/ws/chat")
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, socketListener)

        isConnected = true
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "User Disconnected")
        webSocket = null
        isConnected = false
    }

    fun isConnected() = isConnected

}