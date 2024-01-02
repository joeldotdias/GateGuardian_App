package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.remote.UserApi
import com.example.gateguardianapp.domain.model.User
import com.example.gateguardianapp.domain.repository.UserRepository
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
): UserRepository{

    override suspend fun getUserByEmail(email: String): User? {
        return api.getUserByEmail(email).body()
    }

    override suspend fun saveUser(userRequestBody: RequestBody) {
        api.saveUser(userRequestBody)
    }
}