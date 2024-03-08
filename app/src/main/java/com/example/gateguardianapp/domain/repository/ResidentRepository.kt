package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.data.remote.dto.DashDetailsDto
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto

interface ResidentRepository {

    // App entry
    suspend fun getResidentByEmail(): Resident?

    // Dashboard
    suspend fun getDashProfile(): DashDetailsDto?


    // Admin
    suspend fun getResidentsBySociety(): List<ResidentDto>?
    suspend fun getSecuritiesBySociety(): List<SecurityDto>?
    suspend fun saveResident(residentName: String, residentEmail: String)
    suspend fun saveSecurity(securityName: String, securityEmail: String)

    // Visitors
    suspend fun saveVisitor(name: String, phoneNo: String)
    suspend fun getRecentVisitorOtp(): String?
    suspend fun getVisitorsByResidentEmail(): List<VisitorResidentDto>?

    // Regulars
    suspend fun getRegularsByResidentEmail(): List<RegularsSchema>?
    suspend fun saveRegular(name: String, role: String, entry: String)
    suspend fun getRecentRegularOtp(): String?

    // Notices
    suspend fun getNotices(): List<NoticeDto>?
    suspend fun addNotice(title: String, body: String, category: String)

    // Resident Profile
    suspend fun saveResidentHomeDetails(flatNo: String, building: String)
    suspend fun updateResidentPfp(pfpUrl: String)
    suspend fun updateResidentProfile(aboutMe: String, phoneNo: String)
    suspend fun getMemoriesByResident(email: String): List<EventMemory>?
}