package com.priyanshparekh.ping.addchat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshparekh.ping.R
import com.priyanshparekh.ping.auth.ErrorToast

@Composable
fun AddChatScreen(
    addChatUiState: AddChatUiState,
    onQueryChange: (String) -> Unit,
    onAddChatResultClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                value = addChatUiState.query,
                onValueChange = onQueryChange,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.search_24dp),
                        contentDescription = "Search"
                    )
                },
                placeholder = {
                    Text("Search")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(addChatUiState.searchResult) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(10.dp)
                            .clickable(enabled = true, onClick = {
                                onAddChatResultClick(user.id)
                            })
                    ) {
                        Text(user.name)
                    }
                }
            }

            if (addChatUiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(20.dp))
                ErrorToast(
                    errorMessage = addChatUiState.errorMessage
                )
            }
        }

    }

}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddChatScreenPreview() {
    AddChatScreen(AddChatUiState(), {}, {})
}