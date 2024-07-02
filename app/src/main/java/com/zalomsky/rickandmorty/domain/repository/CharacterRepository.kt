package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import com.zalomsky.rickandmorty.network.api.CharacterApi
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterApi: CharacterApi
) {

    suspend fun getCharactersList(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): CharacterResponse {
        return characterApi.getCharactersList(page, name, status, species, gender)
    }

    suspend fun getCharacterById(id: Int) = characterApi.getCharacterById(id)
}