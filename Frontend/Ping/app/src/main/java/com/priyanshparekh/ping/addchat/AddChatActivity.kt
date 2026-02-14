package com.priyanshparekh.ping.addchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.priyanshparekh.ping.addchat.dto.AddChatRequest
import com.priyanshparekh.ping.ui.theme.PingTheme

class AddChatActivity : ComponentActivity() {

    val addChatViewModel: AddChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val uiState = addChatViewModel.addChatUiState.collectAsState()

            PingTheme {
                AddChatScreen(
                    addChatUiState = uiState.value,
                    onQueryChange = { query ->

                        addChatViewModel.onQueryChange(query)

                        when (query.length) {
                            in 0 until 3 -> {
                                return@AddChatScreen
                            }
                            3 -> addChatViewModel.search(query)
                            else -> addChatViewModel.filter(query)
                        }
                    },
                    onAddChatResultClick = { userId ->
                        addChatViewModel.addNewChat(userId)
                    }
                )
            }
        }
    }
}