package com.example.gateguardianapp.presentation.resident.dashboard

import com.example.gateguardianapp.data.remote.dto.DashDetailsDto
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto

data class DashboardScreenState(
    val dashDetails: DashDetailsDto? = null,
    val visitors: List<VisitorResidentDto>? = null,
    val notices: List<NoticeDto>? = null,
    val errorMessage: String? = null
)
