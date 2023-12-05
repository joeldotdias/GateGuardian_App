package com.example.gateguardianapp.data.remote

import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ResidentApi {

    @GET("/resident")
    suspend fun getResidentByEmail(@Query("email") email: String): Response<Resident>

    @PUT("/update-pfp")
    suspend fun updateResidentPfp(@Query("email") email: String, @Query("pfpUrl") pfpUrl: String)

    @PUT("update-profile")
    suspend fun updateResidentProfile(
        @Query("email") email: String,
        @Query("name") name: String,
        @Query("aboutMe") aboutMe: String,
        @Query("phoneNo") phoneNo: String
    )

    @GET("resident-memories")
    suspend fun getMemoriesByResident(@Query("email") email: String): Response<List<EventMemory>>
}