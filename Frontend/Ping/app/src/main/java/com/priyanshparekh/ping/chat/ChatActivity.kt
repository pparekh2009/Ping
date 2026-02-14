package com.priyanshparekh.ping.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.priyanshparekh.ping.ui.theme.PingTheme

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val chatId = intent.getLongExtra("chatId", -1L)
        val name = intent.getStringExtra("name") ?: ""

        val chatViewModel: ChatViewModel by viewModels()

        setContent {

            val uiState = chatViewModel.chatUiState.collectAsState()

            PingTheme {
                Scaffold { innerPadding ->
                    ChatScreen(
                        name = name,
                        chatUiState = uiState.value,
                        onMessageInputChange = chatViewModel::onMessageInputChange,
                        onSendClick = {
                            chatViewModel.sendMessage(chatId)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }

        chatViewModel.getMessageHistory(chatId)
    }
}