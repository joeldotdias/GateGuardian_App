package com.example.gateguardianapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OopsieScreen(
    backToSignIn: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        AlertDialog(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            icon = {
                   Icon(
                       imageVector = Icons.Rounded.Error,
                       modifier = Modifier.size(40.dp),
                       contentDescription = "Sign In Error icon"
                   )
            },
            title = {
                Text(
                    text = "You have not been registered yet",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier.padding(15.dp),
                        onClick = backToSignIn
                    ) {
                        Text(text = "Back to Sign In")
                    }
                }
            },
            onDismissRequest = {},
            confirmButton = {}
        )
    }
}