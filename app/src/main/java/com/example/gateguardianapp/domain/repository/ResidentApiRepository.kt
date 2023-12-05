package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident

interface ResidentApiRepository {

    suspend fun getResidentByEmail(email: String): Resident?

    suspend fun updateResidentPfp(email: String, pfpUrl: String)

    suspend fun updateResidentProfile(email: String, name: String, aboutMe: String, phoneNo: String)

    suspend fun getMemoriesByResident(email: String): List<EventMemory>?
}