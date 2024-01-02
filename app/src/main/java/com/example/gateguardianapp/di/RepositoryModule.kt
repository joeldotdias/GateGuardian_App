package com.example.gateguardianapp.di

import com.example.gateguardianapp.data.repository.ResidentRepositoryImpl
import com.example.gateguardianapp.data.repository.SecurityRepositoryImpl
import com.example.gateguardianapp.data.repository.UserRepositoryImpl
import com.example.gateguardianapp.domain.repository.ResidentRepository
import com.example.gateguardianapp.domain.repository.SecurityRepository
import com.example.gateguardianapp.domain.repository.UserRepository
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
        userApiRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindResidentApiRepository(
        residentApiRepositoryImpl: ResidentRepositoryImpl
    ): ResidentRepository

    @Binds
    @Singleton
    abstract fun bindSecurityApiRepository(
        securityApiRepositoryImpl: SecurityRepositoryImpl
    ): SecurityRepository
}