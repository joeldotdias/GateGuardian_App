package com.example.gateguardianapp.presentation.security.profile

import com.example.gateguardianapp.domain.model.security.Security

data class SecurityProfileState(
    val security: Security? = null,
    val errorMessage: String? = null
)