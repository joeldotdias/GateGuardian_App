package com.example.gateguardianapp.domain.repository

import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto

interface SecurityApiRepository {

    // App entry
    suspend fun getSecurityByEmail(email: String): Security?

    // Verify visitors
    suspend fun getVisitorsBySociety(email: String): List<VisitorSecurityDto>?

    // Security profile
    suspend fun updateSecurityPfp(email: String, pfpUrl: String)
    suspend fun updateSecurityProfile(email: String, name: String, badgeId: String, phoneNo: String)
}