package com.priyanshparekh.ping.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.priyanshparekh.ping.Ping.Companion.dataStoreManager
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.WebSocketManager
import com.priyanshparekh.ping.websocket.SocketEvent
import com.priyanshparekh.ping.websocket.SocketEventPayload.ChatMessagePayload
import com.priyanshparekh.ping.websocket.SocketEventType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val tag = "Chat_View_Model"

    private val _chatUiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    private val chatRepository = ChatRepository()

    init {
        observeIncomingMessages()
    }

    fun onMessageInputChange(messageInput: String) {
        _chatUiState.update {
            it.copy(messageInput = messageInput)
        }
    }

    fun sendChatMessage(chatId: Long) {
        viewModelScope.launch {
            var chatMessageInput = _chatUiState.value.messageInput

            if (chatMessageInput.isBlank()) {
                chatMessageInput = "Sample Message"
            }

            val userId = dataStoreManager.getUserId().first() as Long

            val chatMessagePayload = ChatMessagePayload(-1L, chatId, userId, chatMessageInput)
            val chatMessagePayloadJson = Gson().toJsonTree(chatMessagePayload)

            val socketEvent = SocketEvent(SocketEventType.CHAT_MESSAGE, chatMessagePayloadJson)

            val messagePayloadString = Gson().toJson(socketEvent)
            Log.d(tag, "sendMessage: messagePayloadString: $messagePayloadString")

            WebSocketManager.send(messagePayloadString)

            val chatMessageUi = ChatMessageUi(chatMessageInput, isOutgoing = true, chatMessageStatus = ChatMessageStatus.SENT)

            _chatUiState.update {
                it.copy(chatMessageUis = it.chatMessageUis + chatMessageUi)
            }
        }
    }

    fun observeIncomingMessages() {
        Log.d(tag, "observeIncomingMessages: called")
        viewModelScope.launch {
            WebSocketManager.event.collect { message ->

                Log.d(tag, "observeIncomingMessages: event collect: $message")

                val socketEvent = Gson().fromJson(message, SocketEvent::class.java)
                val currentUserId = dataStoreManager.getUserId().first()

                val chatMessagePayloadJson = socketEvent.payload
                val chatMessagePayload = Gson().fromJson(chatMessagePayloadJson, ChatMessagePayload::class.java)

                _chatUiState.update {
                    it.copy(chatMessageUis = it.chatMessageUis + ChatMessageUi(chatMessagePayload.message, isOutgoing = chatMessagePayload.senderId == currentUserId, ChatMessageStatus.SENT))
                }
            }
        }
    }

    fun getMessageHistory(chatId: Long) {
        viewModelScope.launch {
            val response = chatRepository.getMessages(chatId)

            when (response) {
                is ApiResponse.SUCCESS -> {
                    _chatUiState.update {
                        it.copy(chatMessageUis = response.data)
                    }
                }
                is ApiResponse.ERROR -> {
                    Log.d(tag, "getMessageHistory: error: ${response.message}")
                    _chatUiState.update {
                        it.copy(errorMessage = response.message)
                    }
                }
            }
        }
    }
}