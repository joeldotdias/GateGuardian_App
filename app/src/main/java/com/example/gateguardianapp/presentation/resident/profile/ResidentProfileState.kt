package com.example.gateguardianapp.presentation.resident.profile

import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident

data class ResidentProfileState(
    val resident: Resident? = null,
    val eventMemories: List<EventMemory>? = null,
    val errorMessage: String? = null
)