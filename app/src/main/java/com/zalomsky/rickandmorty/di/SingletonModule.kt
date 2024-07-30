package com.zalomsky.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.zalomsky.rickandmorty.data.AppDatabase
import com.zalomsky.rickandmorty.domain.models.converters.Converters
import com.zalomsky.rickandmorty.domain.models.mappers.CharacterMapper
import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.network.api.EpisodesApi
import com.zalomsky.rickandmorty.network.api.LocationsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideAppDate(@ApplicationContext appContext: Context, converters: Converters): AppDatabase{
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "new_db")
            .addTypeConverter(converters)
            .build()
    }

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

    @Singleton
    @Provides
    fun provideEpisodeApi(retrofit: Builder): EpisodesApi =
        retrofit
            .build()
            .create(EpisodesApi::class.java)

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideConverters(): Converters {
        return Converters()
    }

    @Provides
    @Singleton
    fun provideCharacterMapper(): CharacterMapper {
        return CharacterMapper()
    }
}