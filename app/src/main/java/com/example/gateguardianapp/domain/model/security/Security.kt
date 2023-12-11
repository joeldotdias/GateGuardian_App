package com.example.gateguardianapp.domain.model.security

data class Security(
    val name: String,
    val email: String,
    val pfpUrl: String,
    val phoneNo: String,
    val badgeId: String,
    val society: String
)