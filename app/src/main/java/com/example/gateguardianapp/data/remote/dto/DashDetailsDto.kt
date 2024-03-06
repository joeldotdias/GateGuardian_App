package com.example.gateguardianapp.data.remote.dto

import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto

data class DashDetailsDto(
    val name: String? = null,
    val flatNo: Int? = null,
    val building: String? = null,
    val pfpUrl: String? = null
)
