package com.example.gateguardianapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
data class VisitorEntity(

    @PrimaryKey
    val visitorId: Int,

    val name: String,
    val hostFlat: Int,
    val hostBuilding: String,
    val society: String,
    val otp: String
)