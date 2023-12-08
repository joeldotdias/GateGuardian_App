package com.example.gateguardianapp.presentation.resident.visitors

import com.example.gateguardianapp.domain.model.resident.Visitor

data class VisitorsState(
    val visitors: List<Visitor>? = null,
    val errorMessage: String? = null
)