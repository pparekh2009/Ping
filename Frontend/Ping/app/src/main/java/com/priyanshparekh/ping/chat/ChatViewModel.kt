package com.priyanshparekh.ping.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.priyanshparekh.ping.Ping.Companion.dataStoreManager
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.WebSocketManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val _chatUiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState

    private val chatRepository = ChatRepository()

    init {
        updateMessages()
    }

    fun onMessageInputChange(messageInput: String) {
        _chatUiState.update {
            it.copy(messageInput = messageInput)
        }
    }

    fun sendMessage(chatId: Long) {
        viewModelScope.launch {
            var messageContent = _chatUiState.value.messageInput

            if (messageContent.isBlank()) {
                messageContent = "Sample Message"
            }

            val userId = dataStoreManager.getUserId().first() as Long
            val messagePayload = MessagePayload(MessageType.CHAT_MESSAGE, userId, chatId, messageContent)

            WebSocketManager.send(Gson().toJson(messagePayload))

            val message = Message(messageContent, isSent = true)

            _chatUiState.update {
                it.copy(messages = it.messages + message)
            }
        }
    }

    fun updateMessages() {
        viewModelScope.launch {
            WebSocketManager.message.collect { message ->

                Log.d("TAG", "chatViewModel: getMessages: message: $message")

                if (message.isEmpty()) {
                    Log.d("TAG", "chatViewModel: getMessages: Message empty")
                    return@collect
                }

                Log.d("TAG", "chatViewModel: getMessages: Message not empty")

                val textMessage = Gson().fromJson(message, MessagePayload::class.java).payload

                _chatUiState.update {
                    it.copy(messages = it.messages + Message(textMessage, false))
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
                        it.copy(messages = response.data)
                    }
                }
                is ApiResponse.ERROR -> {
                    _chatUiState.update {
                        it.copy(errorMessage = response.message)
                    }
                }
            }
        }
    }
}