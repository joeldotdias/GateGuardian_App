package com.example.gateguardianapp.di

import com.example.gateguardianapp.data.repository.UserApiRepositoryImpl
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
    abstract fun bindApiRepository(
        userApiRepositoryImpl: UserApiRepositoryImpl
    ): UserApiRepository
}