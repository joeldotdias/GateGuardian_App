package com.example.gateguardianapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitor_search_entities")
data class VisitorSearchEntity(
    @PrimaryKey
    val visitorId: Int,

    val name: String,
    val flat: Int,
    val building: String
)