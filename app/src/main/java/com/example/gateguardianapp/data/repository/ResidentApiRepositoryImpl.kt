package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.ResidentApi
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

}