package com.zalomsky.rickandmorty.domain.repository

import android.content.Context
import com.zalomsky.rickandmorty.data.CharacterDao
import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import com.zalomsky.rickandmorty.domain.model.Episode
import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.network.isInternetAvailable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao,
    private val context: Context
) {

    suspend fun getCharactersList(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): List<CharacterEntity> {
        return if (isInternetAvailable(context)) {
            val response = characterApi.getCharactersList(page, name, status, species, gender)
            val characters = response.results.map { it.toCharacterEntity() }
            characterDao.insertCharacters(characters)
            characters
        } else {
            characterDao.getAllCharacters()
        }
    }


    suspend fun fetchEpisodes(urls: List<String>): List<Episode> = coroutineScope {
        val deferredEpisodes = urls.map { url ->
            async {
                characterApi.getEpisode(url)
            }
        }
        deferredEpisodes.awaitAll()
    }

    suspend fun getCharacterById(id: Int): CharacterEntity {
        return if (isInternetAvailable(context)) {
            val character = characterApi.getCharacterById(id)
            characterDao.insertCharacters(listOf(character.toCharacterEntity()))
            character.toCharacterEntity()
        } else {
            characterDao.getCharacterById(id)
        }
    }
}