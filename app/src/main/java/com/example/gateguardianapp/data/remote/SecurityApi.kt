package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorLog
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface SecurityApi {

    // App entry
    @GET("sign-in")
    suspend fun getSecurityByEmail(@Header("email") email: String): Response<Security>

    // Verify visitors
    @GET("visitors")
    suspend fun getVisitorsBySociety(@Header("email") email: String): Response<List<VisitorSecurityDto>>

    @DELETE("visitor-verified")
    suspend fun moveVerifiedVisitorToLogs(@Query("id") visitorId: Int)

    // Visitor logs
    @GET("visitor-logs")
    suspend fun getVisitorLogsBySociety(@Header("email") email: String): Response<List<VisitorLog>>

    // Security profile
    @PUT("update-pfp")
    suspend fun updateSecurityPfp(@Header("email") email: String, @Query("pfpUrl") pfpUrl: String)

    @PUT("update-profile")
    suspend fun updateSecurityProfile(
        @Header("email") email: String,
        @Query("name") name: String,
        @Query("badgeId") badgeId: String,
        @Query("phoneNo") phoneNo: String
    )
}