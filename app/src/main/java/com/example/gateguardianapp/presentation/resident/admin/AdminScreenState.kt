package com.example.gateguardianapp.presentation.resident.admin

import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto

data class AdminScreenState(
    val residents: List<ResidentDto>? = null,
    val securities: List<SecurityDto>? = null,
    val notices: List<NoticeDto>? = null,
    val errorMessage: String? = null
)