package com.example.gateguardianapp.presentation.resident.notices

import com.example.gateguardianapp.data.remote.dto.NoticeDto

data class NoticesState (
    val visitors: List<NoticeDto>? = null,
    val errorMessage: String? = null
)