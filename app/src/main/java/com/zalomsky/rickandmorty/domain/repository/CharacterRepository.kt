package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.api.CharacterApi
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterApi: CharacterApi
) {

    suspend fun getCharactersList() = characterApi.getCharactersList()

    suspend fun getCharacterById(id: Int) = characterApi.getCharacterById(id)
}