package com.example.gateguardianapp.presentation.security.notify

import com.example.gateguardianapp.data.remote.dto.NotifyResidentsDto

data class NotifyState(
    val callables: List<NotifyResidentsDto>? = null,
    val errorMessage: String? = null
)
