package com.example.gateguardianapp.data.repository

import com.example.gateguardianapp.data.local.VisitorSearchDao
import com.example.gateguardianapp.data.local.VisitorSearchEntity
import com.example.gateguardianapp.data.mapper.toVisitorSearchEntity
import com.example.gateguardianapp.data.remote.SecurityApi
import com.example.gateguardianapp.data.remote.dto.VerifiedVisitorDto
import com.example.gateguardianapp.data.remote.schema.UpdatePfp
import com.example.gateguardianapp.data.remote.schema.UpdateSecurityProfile
import com.example.gateguardianapp.domain.model.security.Security
import com.example.gateguardianapp.domain.model.security.VisitorLog
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import com.example.gateguardianapp.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SecurityRepositoryImpl @Inject constructor(
    private val api: SecurityApi,
    private val dao: VisitorSearchDao
): SecurityRepository {

    override suspend fun getSecurityByEmail(email: String): Security? {
        return api.getSecurityByEmail(email).body()
    }

    override suspend fun getVisitorsBySociety(email: String): List<VisitorSecurityDto>? {
        val visitors = api.getVisitorsBySociety(email).body()
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

    override suspend fun getVisitorLogs(email: String): List<VisitorLog>? {
        return api.getVisitorLogsBySociety(email).body()
    }


    override suspend fun updateSecurityPfp(email: String, pfpUrl: String) {
        val pfpUrlRequestBody = UpdatePfp(pfpUrl).toRequestBody()
        api.updateSecurityPfp(email, pfpUrlRequestBody)
    }


    override suspend fun updateSecurityProfile(
        email: String,
        badgeId: String,
        phoneNo: String
    ) {
        val profileRequestBody = UpdateSecurityProfile(badgeId, phoneNo).toRequestBody()
        api.updateSecurityProfile(email, profileRequestBody)
    }
}