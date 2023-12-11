package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.Visitor

interface ResidentApiRepository {

    // App entry
    suspend fun getResidentByEmail(email: String): Resident?


    // Admin
    suspend fun getResidentsBySociety(society: String): List<ResidentDto>?
    suspend fun getSecuritiesBySociety(society: String): List<SecurityDto>?
    suspend fun saveResident(name: String, email: String, adminEmail: String)

    //Visitors
    suspend fun saveVisitor(name: String, phoneNo: String, residentEmail: String)
    suspend fun getRecentVisitorOtp(email: String): String?
    suspend fun getVisitorsByResidentEmail(email: String): List<Visitor>?
    suspend fun getVisitorOtpById(visitorId: Int): String?

    // Resident Profile
    suspend fun saveResidentHomeDetails(flatNo: String, building: String, email: String)
    suspend fun updateResidentPfp(email: String, pfpUrl: String)
    suspend fun updateResidentProfile(email: String, name: String, aboutMe: String, phoneNo: String)
    suspend fun getMemoriesByResident(email: String): List<EventMemory>?
}