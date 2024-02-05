package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.ResidentApi
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.data.remote.schema.UpdateResidentHome
import com.example.gateguardianapp.data.remote.schema.UpdateResidentProfile
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto
import com.example.gateguardianapp.domain.repository.ResidentRepository
import javax.inject.Inject

class ResidentRepositoryImpl @Inject constructor(
    private val api: ResidentApi
): ResidentRepository {

    override suspend fun getResidentByEmail(email: String): Resident? {
        return api.getResidentByEmail(email).body()
    }


    override suspend fun getResidentsBySociety(adminEmail: String): List<ResidentDto>? {
        return api.getResidentsBySociety(adminEmail).body()
    }

    override suspend fun getSecuritiesBySociety(adminEmail: String): List<SecurityDto>? {
        return  api.getSecuritiesBySociety(adminEmail).body()
    }

    override suspend fun saveResident(name: String, email: String, adminEmail: String) {
        api.saveResident(adminEmail, name, email)
    }

    override suspend fun saveSecurity(name: String, email: String, adminEmail: String) {
        api.saveSecurity(adminEmail, name, email)
    }


    override suspend fun saveVisitor(name: String, phoneNo: String, residentEmail: String) {
        val visitorResidentDtoRequestBody = VisitorResidentDto(name = name, phoneNo = phoneNo).toRequestBody()
        api.saveVisitor(residentEmail, visitorResidentDtoRequestBody)
    }

    override suspend fun getRecentVisitorOtp(email: String): String? {
        return api.getRecentVisitorOtp(email).body()?.code
    }

    override suspend fun getVisitorsByResidentEmail(email: String): List<VisitorResidentDto>? {
        return api.getVisitorsByResidentEmail(email).body()
    }


    override suspend fun saveResidentHomeDetails(flatNo: String, building: String, email: String) {
        val homeDetailsRequestBody = UpdateResidentHome(flatNo.toInt(), building).toRequestBody()
        api.saveResidentHomeDetails(email, homeDetailsRequestBody)
    }

    override suspend fun updateResidentPfp(email: String, pfpUrl: String) {
        api.updateResidentPfp(email, pfpUrl)
    }

    override suspend fun updateResidentProfile(email: String, aboutMe: String, phoneNo: String) {
        val profileRequestBody = UpdateResidentProfile(aboutMe, phoneNo).toRequestBody()
        api.updateResidentProfile(email, profileRequestBody)
    }

    override suspend fun getMemoriesByResident(email: String): List<EventMemory>? {
        return api.getMemoriesByResident(email).body()
    }
}