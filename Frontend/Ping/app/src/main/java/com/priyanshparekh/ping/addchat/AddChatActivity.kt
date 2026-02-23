package com.priyanshparekh.ping.addchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.priyanshparekh.ping.ui.theme.PingTheme

class AddChatActivity : ComponentActivity() {

    val addChatViewModel: AddChatViewModel by viewModels()

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

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
                    },
                    modifier = Modifier.imePadding().imeNestedScroll()
                )
            }
        }
    }
}