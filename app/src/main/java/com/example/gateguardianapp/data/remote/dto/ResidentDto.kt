package com.example.gateguardianapp.data.remote.dto

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class ResidentDto(
    val name: String,
    val email: String,
    val flatNo: Int,
    val building: String
)