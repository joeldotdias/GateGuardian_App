package com.example.gateguardianapp.presentation.resident.visitors

import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto

data class VisitorsState(
    val visitors: List<VisitorResidentDto>? = null,
    val errorMessage: String? = null
)