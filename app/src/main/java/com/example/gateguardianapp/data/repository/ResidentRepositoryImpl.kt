package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.ResidentApi
import com.example.gateguardianapp.data.remote.dto.DashDetailsDto
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
import com.example.gateguardianapp.data.remote.schema.UpdatePfp
import com.example.gateguardianapp.data.remote.schema.UpdateResidentHome
import com.example.gateguardianapp.data.remote.schema.UpdateResidentProfile
import com.example.gateguardianapp.domain.model.resident.EventMemory
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto
import com.example.gateguardianapp.domain.repository.ResidentRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class ResidentRepositoryImpl @Inject constructor(
    private val api: ResidentApi
): ResidentRepository {

    private val currUserEmail = Firebase.auth.currentUser?.email.toString()

    override suspend fun getResidentByEmail(): Resident? {
        return api.getResidentByEmail(currUserEmail).body()
    }

    override suspend fun getDashProfile(): DashDetailsDto? {
        return api.getDashProfile(currUserEmail).body()
    }


    override suspend fun getResidentsBySociety(): List<ResidentDto>? {
        return api.getResidentsBySociety(currUserEmail).body()
    }

    override suspend fun getSecuritiesBySociety(): List<SecurityDto>? {
        return  api.getSecuritiesBySociety(currUserEmail).body()
    }

    override suspend fun saveResident(residentName: String, residentEmail: String) {
        api.saveResident(currUserEmail, residentName, residentEmail)
    }

    override suspend fun saveSecurity(securityName: String, securityEmail: String) {
        api.saveSecurity(currUserEmail, securityName, securityEmail)
    }


    override suspend fun saveVisitor(name: String, phoneNo: String) {
        val visitorResidentDtoRequestBody = VisitorResidentDto(name = name, phoneNo = phoneNo).toRequestBody()
        api.saveVisitor(currUserEmail, visitorResidentDtoRequestBody)
    }

    override suspend fun getRecentVisitorOtp(): String? {
        return api.getRecentVisitorOtp(currUserEmail).body()?.code
    }

    override suspend fun getVisitorsByResidentEmail(): List<VisitorResidentDto>? {
        return api.getVisitorsByResidentEmail(currUserEmail).body()
    }

    override suspend fun getRegularsByResidentEmail(): List<RegularsSchema>? {
        return api.getRegularsByResidentEmail(currUserEmail).body()
    }

    override suspend fun saveRegular(name: String, role: String, entry: String) {
        val addRegularBody = RegularsSchema(name, role, entry).toRequestBody()
        api.saveRegular(currUserEmail, addRegularBody)
    }

    override suspend fun getRecentRegularOtp(): String? {
        return api.getRecentRegularOtp(currUserEmail).body()?.code
    }

    override suspend fun getNotices(): List<NoticeDto>? {
        return api.getNotices(currUserEmail).body()
    }

    override suspend fun addNotice(title: String, body: String, category: String) {
        val addNoticeRequestBody = NoticeDto(title, body, category).toRequestBody()
        api.addNotice(currUserEmail, addNoticeRequestBody)
    }


    override suspend fun saveResidentHomeDetails(flatNo: String, building: String) {
        val homeDetailsRequestBody = UpdateResidentHome(flatNo.toInt(), building).toRequestBody()
        api.saveResidentHomeDetails(currUserEmail, homeDetailsRequestBody)
    }

    override suspend fun updateResidentPfp(pfpUrl: String) {
        val pfpUrlRequestBody = UpdatePfp(pfpUrl).toRequestBody()
        api.updateResidentPfp(currUserEmail, pfpUrlRequestBody)
    }

    override suspend fun updateResidentProfile(aboutMe: String, phoneNo: String) {
        val profileRequestBody = UpdateResidentProfile(aboutMe, phoneNo).toRequestBody()
        api.updateResidentProfile(currUserEmail, profileRequestBody)
    }

    override suspend fun getMemoriesByResident(email: String): List<EventMemory>? {
        return api.getMemoriesByResident(email).body()
    }
}