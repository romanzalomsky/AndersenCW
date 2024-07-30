package com.zalomsky.rickandmorty.di

import android.content.Context
import com.zalomsky.rickandmorty.data.AppDatabase
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.domain.models.mappers.CharacterMapper

import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import com.zalomsky.rickandmorty.domain.repository.EpisodeRepository
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import com.zalomsky.rickandmorty.network.api.EpisodesApi
import com.zalomsky.rickandmorty.network.api.LocationsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    fun provideCharacterRepository(
        characterApi: CharacterApi,
        characterDao: CharacterDao,
        context: Context,
        mapper: CharacterMapper
    ): CharacterRepository =
        CharacterRepository(characterApi, characterDao, context, mapper)

    @Provides
    fun provideLocationsRepository(locationsApi: LocationsApi): LocationsRepository =
        LocationsRepository(locationsApi)

    @Provides
    fun provideEpisodeRepository(episodesApi: EpisodesApi): EpisodeRepository =
        EpisodeRepository(episodesApi)

    @Provides
    fun provideCharacterDao(appDatabase: AppDatabase): CharacterDao =
        appDatabase.characterDao()

/*
    @Provides
    fun provideEpisodeDao(appDatabase: AppDatabase): EpisodeDao =
        appDatabase.episodeDao()

    @Provides
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao =
        appDatabase.locationDao()*/
}