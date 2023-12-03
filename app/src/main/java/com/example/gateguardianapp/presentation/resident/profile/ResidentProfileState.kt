package com.example.gateguardianapp.presentation.resident.profile

import com.example.gateguardianapp.domain.model.resident.Resident

data class ResidentProfileState(
    val resident: Resident? = null,
    val errorMessage: String? = null
)