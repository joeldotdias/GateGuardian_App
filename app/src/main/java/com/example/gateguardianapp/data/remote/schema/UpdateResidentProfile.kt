package com.example.gateguardianapp.data.remote.schema

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class UpdateResidentProfile(
    val name: String,
    val aboutMe: String,
    val phoneNo: String
) {
    fun toRequestBody(): RequestBody {

        val jsonObject = JSONObject()
        jsonObject.put("name", name)
        jsonObject.put("aboutMe", aboutMe)
        jsonObject.put("phoneNo", phoneNo)

        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}