package com.example.gateguardianapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VisitorSearchEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VisitorSearchDatabase: RoomDatabase() {

    abstract val visitorSearchDao: VisitorSearchDao

    companion object {
        const val DATABASE_NAME = "visitor_search_db"
    }
}