package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.data.local.VisitorSearchEntity
import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorLog
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import kotlinx.coroutines.flow.Flow

interface SecurityRepository {

    // App entry
    suspend fun getSecurityByEmail(email: String): Security?

    // Verify visitors
    suspend fun getVisitorsBySociety(email: String): List<VisitorSecurityDto>?
    suspend fun getVisitorSearchResults(query: String): Flow<List<VisitorSearchEntity>>
    suspend fun moveVerifiedVisitorToLogs(visitorId: Int)

    // Visitor logs
    suspend fun getVisitorLogs(email: String): List<VisitorLog>?

    // Security profile
    suspend fun updateSecurityPfp(email: String, pfpUrl: String)
    suspend fun updateSecurityProfile(email: String, name: String, badgeId: String, phoneNo: String)
}