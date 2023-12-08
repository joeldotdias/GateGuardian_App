package com.example.gateguardianapp.di

import com.example.gateguardianapp.data.remote.ResidentApi
import com.example.gateguardianapp.data.remote.UserApi
import com.example.gateguardianapp.util.Constants
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
    fun provideUserApi(): UserApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideResidentApi(): ResidentApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ResidentApi::class.java)
    }
}