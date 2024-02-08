package com.example.gateguardianapp.data.remote.schema

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class UpdatePfp(
    val pfpUrl: String
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("pfpUrl", pfpUrl)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}
