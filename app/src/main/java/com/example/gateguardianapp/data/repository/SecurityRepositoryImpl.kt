package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.local.VisitorSearchDao
import com.example.gateguardianapp.data.local.VisitorSearchEntity
import com.example.gateguardianapp.data.mapper.toVisitorSearchEntity
import com.example.gateguardianapp.data.remote.SecurityApi
import com.example.gateguardianapp.data.remote.dto.NotifyResidentsDto
import com.example.gateguardianapp.data.remote.dto.VerifiedVisitorDto
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
import com.example.gateguardianapp.data.remote.schema.UpdatePfp
import com.example.gateguardianapp.data.remote.schema.UpdateSecurityProfile
import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorLog
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import com.example.gateguardianapp.domain.repository.SecurityRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    private val api: SecurityApi,
    private val dao: VisitorSearchDao
): SecurityRepository {

    private val currUserEmail = Firebase.auth.currentUser?.email.toString()

    override suspend fun getSecurityByEmail(): Security? {
        return api.getSecurityByEmail(currUserEmail).body()
    }

    override suspend fun getVisitorsBySociety(): List<VisitorSecurityDto>? {
        val visitors = api.getVisitorsBySociety(currUserEmail).body()
        dao.clearVisitorSearchEntities()
        visitors?.let {
            it.forEach { visitorSecurityDto ->
                dao.upsertVisitor(visitorSecurityDto.toVisitorSearchEntity())
            }
        }
        return visitors
    }

    override suspend fun getVisitorSearchResults(query: String): Flow<List<VisitorSearchEntity>> {
        if(query.isEmpty()) {
            return dao.getAllVisitorSearchResults()
        }
        return dao.getVisitorSearchResultsByQuery(query)
    }

    override suspend fun moveVerifiedVisitorToLogs(visitorId: Int) {
        api.moveVerifiedVisitorToLogs(VerifiedVisitorDto(visitorId).toRequestBody())
    }

    override suspend fun getResidentsToNotify(flatNo: Int, building: String): List<NotifyResidentsDto>? {
        return api.getResidentsToNotify(currUserEmail, flatNo, building).body()
    }

    override suspend fun getVisitorLogs(): List<VisitorLog>? {
        return api.getVisitorLogsBySociety(currUserEmail).body()
    }

    override suspend fun getRegulars(): List<RegularsSchema>? {
        return api.getRegularsBySociety(currUserEmail).body()
    }


    override suspend fun updateSecurityPfp(pfpUrl: String) {
        val pfpUrlRequestBody = UpdatePfp(pfpUrl).toRequestBody()
        api.updateSecurityPfp(currUserEmail, pfpUrlRequestBody)
    }


    override suspend fun updateSecurityProfile(name: String, phoneNo: String) {
        val profileRequestBody = UpdateSecurityProfile(name, phoneNo).toRequestBody()
        api.updateSecurityProfile(currUserEmail, profileRequestBody)
    }
}