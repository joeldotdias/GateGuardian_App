package com.example.gateguardianapp.data.remote.schema

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class RegularsSchema(
    val name: String,
    val role: String,
    val entry: String,
    val code: String = ""
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("role", role)
        jsonObject.put("entry", entry)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}
