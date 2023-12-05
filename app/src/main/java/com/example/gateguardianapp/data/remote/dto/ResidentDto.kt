package com.example.gateguardianapp.data.remote.dto

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class ResidentDto(
    val name: String,
    val email: String,
    val society: String
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("email", email)
        jsonObject.put("society", society)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}