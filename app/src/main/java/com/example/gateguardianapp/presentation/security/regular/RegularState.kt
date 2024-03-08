package com.example.gateguardianapp.presentation.security.regular

import com.example.gateguardianapp.data.remote.schema.RegularsSchema

data class RegularState(
    val regulars: List<RegularsSchema>? = null,
    val errorMessage: String? = null
)
