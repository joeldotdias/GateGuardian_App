package com.example.gateguardianapp.presentation.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick:() -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val content = LocalContext.current
    var isLoginFormVisible by remember { mutableStateOf(false) }
    var societyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                content,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onSignInClick
        ) {
            Text(text = "Sign In")
        }

        Button(
            onClick = {
                isLoginFormVisible = true
            }
        ) {
            Text(text = "Register as Admin")
        }

        AnimatedVisibility(visible = isLoginFormVisible) {
            Column {
                OutlinedTextField(
                    value = societyName,
                    onValueChange = { societyName = it },
                    label = { Text(text = "Society Name")
                    }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") },
                    supportingText = { Text(text = "*choose the same email while signing in") }
                )
                Button(
                    onClick = {
                        viewModel.saveUser(email, "Admin")
                        isLoginFormVisible = false
                    }
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}