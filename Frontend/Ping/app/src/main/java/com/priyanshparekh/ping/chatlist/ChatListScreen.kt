package com.priyanshparekh.ping.chatlist

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.priyanshparekh.ping.R

@Composable
fun ChatListScreen(
    chatListUiState: ChatListUiState,
    onAddChatClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onChatClick: (Long, String) -> Unit,
    onSearchInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddChatClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_add_24),
                    contentDescription = ""
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = "Chats",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                PingDropdownMenu(onLogoutClick = onLogoutClick)
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(50.dp)),
                value = chatListUiState.searchInput,
                onValueChange = { input ->
                    onSearchInputChange(input)
                },
                placeholder = {
                    Text("Search")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {
                items(chatListUiState.chatList) { chat ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(60.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(5.dp)
                            .clickable(enabled = true, onClick = {
                                Log.d("TAG", "ChatListScreen: chatId: ${chat.chatId}")
                                onChatClick(chat.chatId, chat.name)
                            }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            painter = painterResource(R.drawable.outline_account_circle_24),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = chat.name,
                            fontSize = 20.sp
                        )
                    }
                    HorizontalDivider(thickness = 2.dp)
                }
            }
        }
    }
}

@Composable
fun PingDropdownMenu(onLogoutClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = onLogoutClick
            )
        }
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ChatListScreenPreview() {
    ChatListScreen(ChatListUiState(), {}, {}, {_, _ ->}, {})
}