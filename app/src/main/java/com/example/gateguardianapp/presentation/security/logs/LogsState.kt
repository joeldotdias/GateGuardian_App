package com.example.gateguardianapp.presentation.security.logs

import com.example.gateguardianapp.domain.model.security.VisitorLog

data class LogsState(
    val visitorLogs: List<VisitorLog>? = null,
    val errorMessage: String? = null
)