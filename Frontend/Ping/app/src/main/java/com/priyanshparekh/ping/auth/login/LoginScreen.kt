package com.priyanshparekh.ping.auth.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshparekh.ping.auth.AuthHeader
import com.priyanshparekh.ping.auth.ErrorToast
import com.priyanshparekh.ping.ui.theme.PingTheme

@Composable
fun LoginScreen(
    loginUiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onToSignUpClick: () -> Unit
) {
    PingTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(20.dp, 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthHeader(header = "Login")

                Spacer(modifier = Modifier.height(20.dp))

                LoginForm(
                    modifier = Modifier.weight(1f),
                    loginUiState = loginUiState,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                )

                Spacer(modifier = Modifier.height(20.dp))

                LoginButton(onLoginClick = onLoginClick, onToSignUpClick = onToSignUpClick)

                if (loginUiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    ErrorToast(errorMessage = loginUiState.errorMessage)
                }
            }

        }
    }
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    loginUiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = loginUiState.email,
            onValueChange = onEmailChange,
            label = {
                Text(text = "Email")
            },
            placeholder = {
                Text("Enter Email")
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = loginUiState.password,
            onValueChange = onPasswordChange,
            label = {
                Text(text = "Password")
            },
            placeholder = {
                Text("Enter Password")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )
    }
}

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onToSignUpClick: () -> Unit
) {
    Button(
        onClick = onLoginClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp))
    ) {
        Text("Login")
    }

    Spacer(modifier = modifier.height(12.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onToSignUpClick),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Don't have an account yet ?")

        Spacer(modifier = modifier.width(12.dp))

        Text("Sign Up")
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginScreenPreview() {
    PingTheme {
        LoginScreen(LoginUiState(), {}, {}, {}, {})
    }

}