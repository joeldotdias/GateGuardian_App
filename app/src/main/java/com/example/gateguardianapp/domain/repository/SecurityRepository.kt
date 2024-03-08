package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.data.local.VisitorSearchEntity
import com.example.gateguardianapp.data.remote.dto.NotifyResidentsDto
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorLog
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import kotlinx.coroutines.flow.Flow

interface SecurityRepository {

    // App entry
    suspend fun getSecurityByEmail(): Security?

    // Verify visitors
    suspend fun getVisitorsBySociety(): List<VisitorSecurityDto>?
    suspend fun getVisitorSearchResults(query: String): Flow<List<VisitorSearchEntity>>
    suspend fun moveVerifiedVisitorToLogs(visitorId: Int)

    //Notify
    suspend fun getResidentsToNotify(flatNo: Int, building: String): List<NotifyResidentsDto>?

    // Visitor logs
    suspend fun getVisitorLogs(): List<VisitorLog>?

    // Regulars
    suspend fun getRegulars(): List<RegularsSchema>?

    // Security profile
    suspend fun updateSecurityPfp(pfpUrl: String)
    suspend fun updateSecurityProfile(name: String, phoneNo: String)
}