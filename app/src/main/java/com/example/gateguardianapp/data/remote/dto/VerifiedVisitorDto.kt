package com.example.gateguardianapp.data.remote.dto

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class VerifiedVisitorDto(
    val visitorId: Int
) {

    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("visitorId", visitorId)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}
