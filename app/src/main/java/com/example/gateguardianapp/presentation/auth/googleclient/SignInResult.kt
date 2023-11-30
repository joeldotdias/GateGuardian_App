package com.example.gateguardianapp.presentation.auth.googleclient

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)