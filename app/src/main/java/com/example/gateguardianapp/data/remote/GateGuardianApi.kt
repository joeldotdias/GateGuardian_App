package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.domain.model.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GateGuardianApi {

    @GET("/user")
    suspend fun getUserByEmail(@Query("email") email: String): Response<User>

    @POST("/save-user")
    suspend fun saveUser(@Body userRequestBody: RequestBody)

    companion object {
        const val BASE_URL = "http://192.168.0.100:8080"
    }
}