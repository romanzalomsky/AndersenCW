package com.zalomsky.rickandmorty.di

import android.content.Context
import com.zalomsky.rickandmorty.data.CharacterDao
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
    fun provideLocationsRepository(locationsApi: LocationsApi): LocationsRepository =
        LocationsRepository(locationsApi)

    @Provides
    fun provideEpisodeRepository(episodesApi: EpisodesApi): EpisodeRepository =
        EpisodeRepository(episodesApi)

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao {
        return database.characterDao()
    }
}