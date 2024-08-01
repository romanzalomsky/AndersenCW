package com.zalomsky.rickandmorty.domain.repository

import android.content.Context
import android.util.Log
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
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

    suspend fun fetchEpisodes(urls: List<String>): List<EpisodeEntity> = coroutineScope {
        val deferredEpisodes = urls.map { url ->
            async {
                if(isInternetAvailable(context)) {
                    characterApi.getEpisode(url)
                } else {
                    /*characterDao.getEpisodesByUrls(urls)*/
                }
            }
        }
        deferredEpisodes.awaitAll()
    } as List<EpisodeEntity>

    suspend fun getFilteredCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): List<CharacterEntity> {
        return characterDao.getFilteredCharacters(name, status, species, gender)
    }

    suspend fun getCharacterById(id: Int): CharacterEntity {
        return if (isInternetAvailable(context)) {
            val character = characterApi.getCharacterById(id)
            characterDao.insertCharacters(listOf(character.toCharacterEntity()))
            character.toCharacterEntity()
            characterDao.getCharacterById(id)
        } else {
            characterDao.getCharacterById(id)
        }
    }
}