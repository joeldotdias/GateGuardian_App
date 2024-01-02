package com.example.gateguardianapp.data.mapper

import com.example.gateguardianapp.data.local.VisitorSearchEntity
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto

fun VisitorSecurityDto.toVisitorSearchEntity(): VisitorSearchEntity {
    return VisitorSearchEntity(
        visitorId = this.visitorId,
        name = this.name,
        flat = this.hostFlat,
        building = this.hostBuilding
    )
}