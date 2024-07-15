package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import com.zalomsky.rickandmorty.domain.model.Episode
import com.zalomsky.rickandmorty.network.api.CharacterApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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

    suspend fun fetchEpisodes(urls: List<String>): List<Episode> = coroutineScope {
        val deferredEpisodes = urls.map { url ->
            async {
                characterApi.getEpisode(url)
            }
        }
        deferredEpisodes.awaitAll()
    }

    suspend fun getCharacterById(id: Int) = characterApi.getCharacterById(id)
}