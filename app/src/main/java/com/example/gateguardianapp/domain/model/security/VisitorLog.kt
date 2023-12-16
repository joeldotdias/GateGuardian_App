package com.example.gateguardianapp.domain.model.security

data class VisitorLog(
    val name: String,
    val hostFlat: Int,
    val hostBuilding: String,
    val entry: String
)