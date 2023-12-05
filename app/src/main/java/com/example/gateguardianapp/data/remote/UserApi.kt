package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.domain.model.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @GET("/user")
    suspend fun getUserByEmail(@Query("email") email: String): Response<User>

    @POST("/user-save")
    suspend fun saveUser(@Body userRequestBody: RequestBody)
}