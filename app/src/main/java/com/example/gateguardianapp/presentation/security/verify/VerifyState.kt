package com.example.gateguardianapp.presentation.security.verify

import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto

data class VerifyState(
    val visitors: List<VisitorSecurityDto>? = null,
    //val visitors: List<VisitorEntity>? = null,
    val errorMessage: String? = null
)