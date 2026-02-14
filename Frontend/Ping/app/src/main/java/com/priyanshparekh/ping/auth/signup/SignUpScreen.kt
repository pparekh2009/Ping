package com.priyanshparekh.ping.auth.signup

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.priyanshparekh.ping.auth.AuthHeader
import com.priyanshparekh.ping.auth.ErrorToast
import com.priyanshparekh.ping.ui.theme.PingTheme

@Composable
fun SignUpScreen(
    signUpUiState: SignUpUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onToLoginClick: () -> Unit
) {
    PingTheme {
        Surface {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(20.dp, 50.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthHeader(header = "Sign Up")

                Spacer(modifier = Modifier.height(20.dp))

                SignUpForm(
                    modifier = Modifier.weight(1f),
                    signUpUiState = signUpUiState,
                    onNameChange = onNameChange,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                )

                Spacer(modifier = Modifier.height(20.dp))

                SignUpButton(onSignUpClick = onSignUpClick, onToLoginClick = onToLoginClick)

                if (signUpUiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(20.dp))

                    ErrorToast(errorMessage = signUpUiState.errorMessage)
                }
            }

        }
    }
}

@Composable
fun SignUpForm(
    modifier: Modifier = Modifier,
    signUpUiState: SignUpUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = signUpUiState.name,
            onValueChange = onNameChange,
            label = {
                Text(text = "Name")
            },
            placeholder = {
                Text("Enter Name")
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
            value = signUpUiState.email,
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
            value = signUpUiState.password,
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
fun SignUpButton(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onToLoginClick: () -> Unit
) {
    Button(
        onClick = onSignUpClick,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(50.dp))
    ) {
        Text("Sign Up")
    }

    Spacer(modifier = modifier.height(12.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onToLoginClick),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Already registered ?")

        Spacer(modifier = modifier.width(12.dp))

        Text("Login")
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier) {
    SignUpScreen(SignUpUiState(), {}, {}, {}, {}, {})
}