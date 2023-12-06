package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident

interface ResidentApiRepository {

    suspend fun getResidentByEmail(email: String): Resident?

    suspend fun getResidentsBySociety(society: String): List<ResidentDto>?

    suspend fun saveResidentHomeDetails(flatNo: String, building: String, email: String)

    suspend fun saveResident(name: String, email: String, adminEmail: String)

    suspend fun updateResidentPfp(email: String, pfpUrl: String)

    suspend fun updateResidentProfile(email: String, name: String, aboutMe: String, phoneNo: String)

    suspend fun getMemoriesByResident(email: String): List<EventMemory>?
}