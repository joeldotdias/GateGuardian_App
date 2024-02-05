package com.example.gateguardianapp.domain.model.resident

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class VisitorResidentDto(
    val visitorId: Int? = null,
    val name: String,
    val phoneNo: String,
    val code: String = ""
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("phoneNo", phoneNo)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}