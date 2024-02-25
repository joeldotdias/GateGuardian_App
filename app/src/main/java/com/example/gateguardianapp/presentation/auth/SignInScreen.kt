package com.example.gateguardianapp.presentation.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.R
import com.example.gateguardianapp.presentation.components.InputForm

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

        Image(
            painter = painterResource(id = R.drawable.gate_guardian_logo),
            modifier = Modifier
                .size(240.dp)
                .padding(15.dp),
            contentDescription = "Gate Guardian logo"
        )

        Surface(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = onSignInClick,
            shape = ShapeDefaults.Medium,
            border = BorderStroke(width = 1.dp, color = Color.Blue),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_logo),
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified,
                    contentDescription = "Google logo"
                )
                Text(
                    text = "Sign in with Google",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Surface(
            onClick = { isLoginFormVisible = !isLoginFormVisible },
            shape = ShapeDefaults.Medium,
            border = BorderStroke(width = 1.dp, color = Color.Blue),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.gate_guardian_society_admin_logo),
                    modifier = Modifier.size(35.dp),
                    tint = Color.Unspecified,
                    contentDescription = "Gate Guardian logo"
                )
                Text(
                    text = "Sign up as Society Admin",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
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
                    leadingIcon = Icons.Rounded.Person,
                    onImeAction =  KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                InputForm(
                    value = email,
                    label = "Email",
                    onValChange = { email = it },
                    leadingIcon = Icons.Rounded.AlternateEmail,
                    keyboardType = KeyboardType.Email,
                    onImeAction =  KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                InputForm(
                    value = society,
                    label = "Society name",
                    onValChange = { society = it },
                    leadingIcon = Icons.Rounded.Apartment,
                    imeAction = ImeAction.Done,
                    onImeAction =  KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { isLoginFormVisible = false }
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            viewModel.saveUser(name, email, "Admin")
                            isLoginFormVisible = false
                        }
                    ) {
                        Text(text = "Register")
                    }
                }
            }
        }
    }
}