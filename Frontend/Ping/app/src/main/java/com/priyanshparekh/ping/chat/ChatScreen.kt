package com.priyanshparekh.ping.chat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.priyanshparekh.ping.R
import com.priyanshparekh.ping.ui.theme.PingTheme

@Composable
fun ChatScreen(
    name: String,
    chatUiState: ChatUiState,
    onMessageInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ChatHeader(name)

            Conversations(
                chatUiState = chatUiState,
                modifier = Modifier
                .fillMaxSize()
                .weight(1f))

            Keyboard(
                messageInput = chatUiState.messageInput,
                onMessageInputChange = onMessageInputChange,
                onSendClick = onSendClick
            )
        }
    }
}

@Composable
fun ChatHeader(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun Keyboard(
    messageInput: String,
    onMessageInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            value = messageInput,
            onValueChange = onMessageInputChange,
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
                .weight(1f),
            placeholder = {
                Text("Enter Message")
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = {
                onSendClick()
                onMessageInputChange("")
            },
            modifier = Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                painter = painterResource(R.drawable.send_24dp),
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun Conversations(chatUiState: ChatUiState, modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer),
        reverseLayout = true
    ) {
        items(chatUiState.messages.asReversed()) { message ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(32.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(message.content)
                    }
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ChatScreenPreview() {
    PingTheme {
        ChatScreen("Username", ChatUiState(), {}, {})
    }
}