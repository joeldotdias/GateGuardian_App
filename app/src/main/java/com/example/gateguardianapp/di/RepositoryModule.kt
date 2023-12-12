package com.example.gateguardianapp.di

import com.example.gateguardianapp.data.repository.ResidentApiRepositoryImpl
import com.example.gateguardianapp.data.repository.SecurityApiRepositoryImpl
import com.example.gateguardianapp.data.repository.UserApiRepositoryImpl
import com.example.gateguardianapp.domain.repository.ResidentApiRepository
import com.example.gateguardianapp.domain.repository.SecurityApiRepository
import com.example.gateguardianapp.domain.repository.UserApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserApiRepository(
        userApiRepositoryImpl: UserApiRepositoryImpl
    ): UserApiRepository

    @Binds
    @Singleton
    abstract fun bindResidentApiRepository(
        residentApiRepositoryImpl: ResidentApiRepositoryImpl
    ): ResidentApiRepository

    @Binds
    @Singleton
    abstract fun bindSecurityApiRepository(
        securityApiRepositoryImpl: SecurityApiRepositoryImpl
    ): SecurityApiRepository
}