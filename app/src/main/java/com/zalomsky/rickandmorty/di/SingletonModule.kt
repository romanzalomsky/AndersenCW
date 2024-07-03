package com.zalomsky.rickandmorty.di

import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.network.api.LocationsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideCharacterApi(retrofit: Retrofit.Builder): CharacterApi =
        retrofit
            .build()
            .create(CharacterApi::class.java)

    @Singleton
    @Provides
    fun provideLocationsApi(retrofit: Builder): LocationsApi =
        retrofit
            .build()
            .create(LocationsApi::class.java)
}