package com.example.gateguardianapp.domain.model.resident

data class EventMemory(
    val authorEmail: String,
    val title: String,
    val body: String,
    val coverUrl: String,
    val postedAt: String
)