package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.GateGuardianApi
import com.example.gateguardianapp.domain.model.User
import com.example.gateguardianapp.domain.repository.ApiRepository
import okhttp3.RequestBody
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val api: GateGuardianApi
): ApiRepository{

    override suspend fun getUserByEmail(email: String): User? {
        return api.getUserByEmail(email).body()
    }

    override suspend fun saveUser(userRequestBody: RequestBody) {
        api.saveUser(userRequestBody)
    }
}