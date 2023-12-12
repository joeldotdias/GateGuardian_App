package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.SecurityApi
import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.repository.SecurityApiRepository
import javax.inject.Inject

class SecurityApiRepositoryImpl @Inject constructor(
    private val api: SecurityApi
): SecurityApiRepository {

    override suspend fun getSecurityByEmail(email: String): Security? {
        return api.getSecurityByEmail(email).body()
    }


    override suspend fun updateSecurityPfp(email: String, pfpUrl: String) {
        api.updateSecurityPfp(email, pfpUrl)
    }

    override suspend fun updateSecurityProfile(
        email: String,
        name: String,
        badgeId: String,
        phoneNo: String
    ) {
        api.updateSecurityProfile(email, name, badgeId, phoneNo)
    }

}