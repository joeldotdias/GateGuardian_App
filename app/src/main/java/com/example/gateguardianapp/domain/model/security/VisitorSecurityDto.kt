package com.example.gateguardianapp.domain.model.security

data class VisitorSecurityDto(
    val visitorId: Int,
    val name: String,
    val hostFlat: Int,
    val hostBuilding: String,
    val society: String,
    val code: String,
    var isVerified: Boolean? = null
)