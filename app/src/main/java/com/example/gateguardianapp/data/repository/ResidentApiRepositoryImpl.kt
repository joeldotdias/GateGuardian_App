package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.ResidentApi
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.Visitor
import com.example.gateguardianapp.domain.repository.ResidentApiRepository
import javax.inject.Inject

class ResidentApiRepositoryImpl @Inject constructor(
    private val api: ResidentApi
): ResidentApiRepository {

    override suspend fun getResidentByEmail(email: String): Resident? {
        return api.getResidentByEmail(email).body()
    }


    override suspend fun getResidentsBySociety(society: String): List<ResidentDto>? {
        return api.getResidentsBySociety(society).body()
    }

    override suspend fun saveResident(name: String, email: String, adminEmail: String) {
        api.saveResident(name, email, adminEmail)
    }


    override suspend fun saveVisitor(name: String, phoneNo: String, residentEmail: String) {
        val visitorRequestBody = Visitor(name = name, phoneNo = phoneNo, residentEmail = residentEmail).toRequestBody()
        api.saveVisitor(visitorRequestBody)
    }

    override suspend fun getRecentVisitorOtp(email: String): String? {
        return api.getRecentVisitorOtp(email).body()
    }

    override suspend fun getVisitorsByResidentEmail(email: String): List<Visitor>? {
        return api.getVisitorsByResidentEmail(email).body()
    }

    override suspend fun getVisitorOtpById(visitorId: Int): String? {
        return api.getVisitorOtp(visitorId).body()
    }


    override suspend fun saveResidentHomeDetails(flatNo: String, building: String, email: String) {
        api.saveResidentHomeDetails(flatNo.toInt(), building, email)
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