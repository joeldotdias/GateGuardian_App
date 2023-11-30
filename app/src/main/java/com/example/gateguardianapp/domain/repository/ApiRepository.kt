package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.domain.model.User
import okhttp3.RequestBody
import retrofit2.http.Body

interface ApiRepository {

    suspend fun getUserByEmail(email: String): User?

    suspend fun saveUser(@Body userRequestBody: RequestBody)
}