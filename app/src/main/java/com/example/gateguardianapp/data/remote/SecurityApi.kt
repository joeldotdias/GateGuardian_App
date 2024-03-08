package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.data.remote.dto.NotifyResidentsDto
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorLog
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface SecurityApi {

    // App entry
    @GET("sign-in")
    suspend fun getSecurityByEmail(@Header("email") email: String): Response<Security>

    // Verify visitors
    @GET("visitors")
    suspend fun getVisitorsBySociety(@Header("email") email: String): Response<List<VisitorSecurityDto>>

    @POST("visitor-verified")
    suspend fun moveVerifiedVisitorToLogs(@Body verifiedVisitorBody: RequestBody)

    // Visitor logs
    @GET("visitor-logs")
    suspend fun getVisitorLogsBySociety(@Header("email") email: String): Response<List<VisitorLog>>

    // Regulars
    @GET("regulars")
    suspend fun getRegularsBySociety(@Header("email") email: String): Response<List<RegularsSchema>>

    @GET("notify")
    suspend fun getResidentsToNotify(
        @Header("email") email: String,
        @Query("flatNo") flatNo: Int,
        @Query("building") building: String
    ): Response<List<NotifyResidentsDto>>

    // Security profile
    @PUT("update-pfp")
    suspend fun updateSecurityPfp(@Header("email") email: String, @Body pfpUrlSchema: RequestBody)

    @PUT("update-profile")
    suspend fun updateSecurityProfile(
        @Header("email") email: String,
        @Body profileDetailsSchema: RequestBody
    )
}