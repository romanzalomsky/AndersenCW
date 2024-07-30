package com.zalomsky.rickandmorty.domain.repository

import android.content.Context
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.domain.models.mappers.CharacterMapper
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.network.utils.NetworkUtils.Companion.isInternetAvailable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao,
    private val context: Context,
    private val characterMapper: CharacterMapper
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
            val characters = characterMapper.fromResponseToEntityList(response)
            characterDao.insertCharacters(characters)
            characters
        } else {
            characterDao.getAllCharacters()
        }
    }

    suspend fun fetchEpisodes(urls: List<String>): List<EpisodeEntity> = coroutineScope {
        val deferredEpisodes = urls.map { url ->
            async {
                characterApi.getEpisode(url)
            }
        }
        deferredEpisodes.awaitAll()
    }

    suspend fun getCharacterById(id: Int) = characterApi.getCharacterById(id)

}