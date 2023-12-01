package com.example.gateguardianapp.presentation.resident.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ResidentProfileScreen(
    signOut:() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(onClick = { signOut() }) {
            Text(text = "Sign out")
        }
    }
}