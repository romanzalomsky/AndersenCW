package com.zalomsky.rickandmorty.di

import android.content.Context
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.data.dao.EpisodeDao
import com.zalomsky.rickandmorty.data.dao.LocationDao
import com.zalomsky.rickandmorty.data.db.AppDatabase
import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import com.zalomsky.rickandmorty.domain.repository.EpisodeRepository
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import com.zalomsky.rickandmorty.network.api.EpisodesApi
import com.zalomsky.rickandmorty.network.api.LocationsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    fun provideCharacterRepository(
        characterApi: CharacterApi,
        characterDao: CharacterDao,
        context: Context
    ): CharacterRepository =
        CharacterRepository(characterApi, characterDao, context)

    @Provides
    fun provideLocationsRepository(
        locationsApi: LocationsApi,
        locationDao: LocationDao,
        context: Context
    ): LocationsRepository =
        LocationsRepository(locationsApi, locationDao, context)

    @Provides
    fun provideEpisodeRepository(
        episodesApi: EpisodesApi,
        episodeDao: EpisodeDao,
        context: Context
    ): EpisodeRepository =
        EpisodeRepository(episodesApi, episodeDao, context)

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    fun provideLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    fun provideEpisodeDao(database: AppDatabase): EpisodeDao {
        return database.episodeDao()
    }
}