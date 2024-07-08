package com.zalomsky.rickandmorty.di

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

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideCharacterRepository(characterApi: CharacterApi): CharacterRepository =
        CharacterRepository(characterApi)

    @Provides
    fun provideLocationsRepository(locationsApi: LocationsApi): LocationsRepository =
        LocationsRepository(locationsApi)

    @Provides
    fun provideEpisodeRepository(episodesApi: EpisodesApi): EpisodeRepository =
        EpisodeRepository(episodesApi)
}