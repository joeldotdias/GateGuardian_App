package com.example.gateguardianapp.presentation.resident.admin

import com.example.gateguardianapp.data.remote.dto.ResidentDto

data class AdminScreenState(
    val residents: List<ResidentDto>? = null,
    val errorMessage: String? = null
)