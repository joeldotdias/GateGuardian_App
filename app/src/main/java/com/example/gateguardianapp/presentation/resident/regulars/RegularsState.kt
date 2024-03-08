package com.example.gateguardianapp.presentation.resident.regulars

import com.example.gateguardianapp.data.remote.schema.RegularsSchema

data class RegularsState(
    val regulars: List<RegularsSchema>? = null,
    val errorMessage: String? = null
)
