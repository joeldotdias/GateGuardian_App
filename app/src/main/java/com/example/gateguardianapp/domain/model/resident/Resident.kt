package com.example.gateguardianapp.domain.model.resident

data class Resident(
    val name: String,
    val pfpUrl: String,
    val aboutMe: String,
    val phoneNo: String,
    val flatNo: Int,
    val building: String,
    val society: String
)