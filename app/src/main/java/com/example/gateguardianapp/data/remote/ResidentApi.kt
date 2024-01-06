package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ResidentApi {

    // App entry
    @GET("sign-in")
    suspend fun getResidentByEmail(@Header("email") email: String): Response<Resident>


    // Visitors
    @GET("visitors")
    suspend fun getVisitorsByResidentEmail(@Header("email") email: String): Response<List<VisitorResidentDto>>

    @POST("visitor-save")
    suspend fun saveVisitor(@Body visitorRequestBody: RequestBody)

    @GET("visitor-recent")
    suspend fun getRecentVisitorOtp(@Header("email") email: String): Response<String>

    @GET("visitor-otp")
    suspend fun getVisitorOtp(@Query("visitorId") visitorId: Int): Response<String>


    // Profile
    @PUT("update-home")
    suspend fun saveResidentHomeDetails(
        @Header("email") email: String,
        @Query("flat") flatNo: Int,
        @Query("building") building: String
    )

    @PUT("update-pfp")
    suspend fun updateResidentPfp(@Header("email") email: String, @Query("pfpUrl") pfpUrl: String)

    @PUT("update-profile")
    suspend fun updateResidentProfile(
        @Header("email") email: String,
        @Query("name") name: String,
        @Query("aboutMe") aboutMe: String,
        @Query("phoneNo") phoneNo: String
    )

    @GET("memories")
    suspend fun getMemoriesByResident(@Header("email") email: String): Response<List<EventMemory>>


    // Admin
    @GET("admin/residents")
    suspend fun getResidentsBySociety(@Header("admin") email: String): Response<List<ResidentDto>>

    @POST("admin/save-resident")
    suspend fun saveResident(
        @Header("admin") adminEmail: String,
        @Query("name") name: String,
        @Query("email") email: String
    )

    @GET("admin/securities")
    suspend fun getSecuritiesBySociety(@Header("admin") email: String): Response<List<SecurityDto>>

    @POST("admin/save-security")
    suspend fun saveSecurity(
        @Header("admin") adminEmail: String,
        @Query("name") name: String,
        @Query("email") email: String
    )
}