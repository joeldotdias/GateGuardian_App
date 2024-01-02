package com.example.gateguardianapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitorSearchDao {

    @Query("SELECT * FROM visitor_search_entities")
    fun getAllVisitorSearchResults(): Flow<List<VisitorSearchEntity>>

    @Query("SELECT * FROM visitor_search_entities WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    fun getVisitorSearchResultsByQuery(query: String): Flow<List<VisitorSearchEntity>>

    @Upsert
    suspend fun insertVisitors(visitors: List<VisitorSearchEntity>)

    @Upsert
    suspend fun upsertVisitor(visitorSearchEntity: VisitorSearchEntity)

    @Query("DELETE FROM visitor_search_entities")
    suspend fun clearVisitorSearchEntities()
}