package com.example.gateguardianapp.di

import android.app.Application
import androidx.room.Room
import com.example.gateguardianapp.data.local.VisitorSearchDatabase
import com.example.gateguardianapp.data.remote.ResidentApi
import com.example.gateguardianapp.data.remote.SecurityApi
import com.example.gateguardianapp.data.remote.UserApi
import com.example.gateguardianapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideResidentApi(): ResidentApi {
        return Retrofit.Builder()
            .baseUrl("${Constants.BASE_URL}/resident/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ResidentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSecurityApi(): SecurityApi {
        return Retrofit.Builder()
            .baseUrl("${Constants.BASE_URL}/security/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(SecurityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVisitorSearchDatabase(app: Application): VisitorSearchDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = VisitorSearchDatabase::class.java,
            name = VisitorSearchDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideVisitorSearchScreenDao(visitorSearchDatabase: VisitorSearchDatabase)
        = visitorSearchDatabase.visitorSearchDao
}