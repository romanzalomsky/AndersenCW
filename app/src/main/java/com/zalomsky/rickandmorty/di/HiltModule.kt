package com.zalomsky.rickandmorty.di

import com.zalomsky.rickandmorty.api.CharacterApi
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
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
}