package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.Visitor
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ResidentApi {

    // App entry
    @GET("/resident")
    suspend fun getResidentByEmail(@Query("email") email: String): Response<Resident>


    // Admin
    @GET("/residents")
    suspend fun getResidentsBySociety(@Query("admin") email: String): Response<List<ResidentDto>>

    @GET("/securities")
    suspend fun getSecuritiesBySociety(@Query("admin") email: String): Response<List<SecurityDto>>

    @POST("/resident-save")
    suspend fun saveResident(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("admin") adminEmail: String
    )

    @POST("/security-save")
    suspend fun saveSecurity(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("admin") adminEmail: String
    )


    // Visitors
    @GET("/visitors")
    suspend fun getVisitorsByResidentEmail(@Query("email") email: String): Response<List<Visitor>>

    @POST("/visitor-save")
    suspend fun saveVisitor(@Body visitorRequestBody: RequestBody)

    @GET("/visitor-recent")
    suspend fun getRecentVisitorOtp(@Query("email") email: String): Response<String>

//    @GET("visitor-recento")
//    suspend fun getRecentVisitorCred(@Query("email") email: String): Response<VisitorCredDto>

    @GET("/visitor-otp")
    suspend fun getVisitorOtp(@Query("visitorId") visitorId: Int): Response<String>


    // Resident Profile
    @POST("/resident-home")
    suspend fun saveResidentHomeDetails(
        @Query("flat") flatNo: Int,
        @Query("building") building: String,
        @Query("email") email: String
    )

    @PUT("/update-pfp")
    suspend fun updateResidentPfp(@Query("email") email: String, @Query("pfpUrl") pfpUrl: String)

    @PUT("/update-profile")
    suspend fun updateResidentProfile(
        @Query("email") email: String,
        @Query("name") name: String,
        @Query("aboutMe") aboutMe: String,
        @Query("phoneNo") phoneNo: String
    )

    @GET("/resident-memories")
    suspend fun getMemoriesByResident(@Query("email") email: String): Response<List<EventMemory>>
}
//
//data class VisitorCredDto(
//    val uid: String,
//    val otp: String
//)