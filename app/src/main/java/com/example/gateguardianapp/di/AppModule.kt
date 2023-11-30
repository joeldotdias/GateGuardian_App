package com.example.gateguardianapp.di

import com.example.gateguardianapp.data.remote.GateGuardianApi
import com.example.gateguardianapp.presentation.auth.googleclient.GoogleAuthClient
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGateGuardianApi(): GateGuardianApi {
        return Retrofit.Builder()
            .baseUrl(GateGuardianApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GateGuardianApi::class.java)
    }
}