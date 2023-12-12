package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.domain.model.security.Security
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface SecurityApi {

    // App entry
    @GET("/security")
    suspend fun getSecurityByEmail(@Query("email") email: String): Response<Security>

    // Security profile
    @PUT("/update-security-pfp")
    suspend fun updateSecurityPfp(@Query("email") email: String, @Query("pfpUrl") pfpUrl: String)

    @PUT("update-security-profile")
    suspend fun updateSecurityProfile(
        @Query("email") email: String,
        @Query("name") name: String,
        @Query("badgeId") badgeId: String,
        @Query("phoneNo") phoneNo: String
    )
}