package com.example.gateguardianapp.presentation.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.presentation.resident.components.InputForm

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick:() -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var isLoginFormVisible by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var society by remember { mutableStateOf("") }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputForm(
                    value = name,
                    label = "Your name",
                    onValChange = { name = it },
                    icon = Icons.Rounded.Person,
                    onImeAction =  KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                InputForm(
                    value = email,
                    label = "Email",
                    onValChange = { email = it },
                    icon = Icons.Rounded.AlternateEmail,
                    keyboardType = KeyboardType.Email,
                    onImeAction =  KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                InputForm(
                    value = society,
                    label = "Society name",
                    onValChange = { society = it },
                    icon = Icons.Rounded.Apartment,
                    imeAction = ImeAction.Done,
                    onImeAction =  KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Button(
                    onClick = {
                        viewModel.saveUser(name, email, "Admin", society)
                        isLoginFormVisible = false
                    }
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}