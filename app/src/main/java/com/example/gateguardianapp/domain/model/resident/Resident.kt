package com.example.gateguardianapp.domain.model.resident

data class Resident(
    val id: Int,
    val name: String,
    val email: String,
    val pfpUrl: String,
    val flatNo: Int,
    val building: String,
    val society: String
)