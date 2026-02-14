package com.priyanshparekh.ping.chatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.priyanshparekh.ping.addchat.AddChatActivity
import com.priyanshparekh.ping.auth.AuthViewModel
import com.priyanshparekh.ping.auth.login.LoginActivity
import com.priyanshparekh.ping.chat.ChatActivity
import com.priyanshparekh.ping.ui.theme.PingTheme

class ChatListActivity : ComponentActivity() {

    val chatListViewModel: ChatListViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val uiState = chatListViewModel.chatListUiState.collectAsState()

            PingTheme {
                ChatListScreen(
                    chatListUiState = uiState.value,
                    onAddChatClick = {
                        startActivity(Intent(this, AddChatActivity::class.java))
                    },
                    onLogoutClick = {
                        authViewModel.logout()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    },
                    onChatClick = { chatId, name ->
                        Log.d("TAG", "chatListActivity: onCreate: chatId: $chatId")
                        val intent = Intent(this, ChatActivity::class.java)
                        intent.putExtra("chatId", chatId)
                        intent.putExtra("name", name)
                        startActivity(intent)
                    },
                    onSearchInputChange = { input ->
                        chatListViewModel.searchInputChange(input)
                    }
                )
            }
        }

        chatListViewModel.getChatList()
    }
}