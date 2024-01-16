package com.example.gateguardianapp.data.remote.schema

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class UpdateResidentHome(
    val flat: Int,
    val building: String
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("flat", flat)
        jsonObject.put("building", building)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}