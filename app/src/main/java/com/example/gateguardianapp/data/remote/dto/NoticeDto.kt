package com.example.gateguardianapp.data.remote.dto

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class NoticeDto(
    val title: String = "",
    val body: String = "",
    val category: String = "",
    val posted: String = ""
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("body", body)
        jsonObject.put("category", category)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}
