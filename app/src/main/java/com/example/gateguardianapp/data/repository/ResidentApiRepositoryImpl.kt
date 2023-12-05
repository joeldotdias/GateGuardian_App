package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.ResidentApi
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.repository.ResidentApiRepository
import javax.inject.Inject

class ResidentApiRepositoryImpl @Inject constructor(
    private val api: ResidentApi
): ResidentApiRepository {

    override suspend fun getResidentByEmail(email: String): Resident? {
        return api.getResidentByEmail(email).body()
    }

    override suspend fun updateResidentPfp(email: String, pfpUrl: String) {
        api.updateResidentPfp(email, pfpUrl)
    }

    override suspend fun updateResidentProfile(email: String, name: String, aboutMe: String, phoneNo: String) {
        api.updateResidentProfile(email, name, aboutMe, phoneNo)
    }

    override suspend fun getMemoriesByResident(email: String): List<EventMemory>? {
        return api.getMemoriesByResident(email).body()
    }
}