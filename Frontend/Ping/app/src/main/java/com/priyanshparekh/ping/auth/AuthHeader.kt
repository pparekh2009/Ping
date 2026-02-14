package com.priyanshparekh.ping.auth

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun AuthHeader(modifier: Modifier = Modifier, header: String) {
    Text(header, fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
}