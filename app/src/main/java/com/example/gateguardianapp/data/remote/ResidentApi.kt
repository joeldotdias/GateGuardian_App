package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.data.remote.dto.DashDetailsDto
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.data.remote.schema.RecentOtp
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
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


    // Dashboard
    @GET("dashboard")
    suspend fun getDashProfile(@Header("email") email: String): Response<DashDetailsDto>


    // Visitors
    @GET("visitors")
    suspend fun getVisitorsByResidentEmail(@Header("email") email: String): Response<List<VisitorResidentDto>>

    @POST("visitor-save")
    suspend fun saveVisitor(
        @Header("email") email: String,
        @Body visitorRequestBody: RequestBody
    )

    @GET("visitor-recent")
    suspend fun getRecentVisitorOtp(@Header("email") email: String): Response<RecentOtp>


    // Regulars
    @GET("regulars")
    suspend fun getRegularsByResidentEmail(@Header("email") email: String): Response<List<RegularsSchema>>

    @POST("regular-save")
    suspend fun saveRegular(
        @Header("email") email: String,
        @Body regularRequestBody: RequestBody
    )

    @GET("regular-recent")
    suspend fun getRecentRegularOtp(@Header("email") email: String): Response<RecentOtp>


    // Profile
    @PUT("update-home")
    suspend fun saveResidentHomeDetails(
        @Header("email") email: String,
        @Body homeDetailsSchema: RequestBody
    )

    @PUT("update-profile")
    suspend fun updateResidentProfile(
        @Header("email") email: String,
        @Body profileDetailsSchema: RequestBody
    )

    @PUT("update-pfp")
    suspend fun updateResidentPfp(@Header("email") email: String, @Body pfpUrlSchema: RequestBody)


    @GET("memories")
    suspend fun getMemoriesByResident(@Header("email") email: String): Response<List<EventMemory>>


    // Admin
    @GET("admin/residents")
    suspend fun getResidentsBySociety(@Header("admin") email: String): Response<List<ResidentDto>>


    // Notices
    @GET("notices")
    suspend fun getNotices(@Header("email") email: String): Response<List<NoticeDto>>


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

    @POST("admin/notice-save")
    suspend fun addNotice(
        @Header("admin") adminEmail: String,
        @Body addNoticeRequest: RequestBody
    )
}