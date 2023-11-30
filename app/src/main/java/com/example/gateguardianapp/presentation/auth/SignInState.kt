package com.example.gateguardianapp.presentation.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)