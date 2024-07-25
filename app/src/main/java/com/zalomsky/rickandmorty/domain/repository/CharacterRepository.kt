package com.zalomsky.rickandmorty.domain.repository

import android.content.Context
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import com.zalomsky.rickandmorty.domain.model.EpisodeEntity
import com.zalomsky.rickandmorty.network.api.CharacterApi
import com.zalomsky.rickandmorty.network.utils.NetworkUtils
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
    ): CharacterResponse {
        return if (NetworkUtils.isInternetAvailable(context)) {
            val response = characterApi.getCharactersList(page, name, status, species, gender)
            characterDao.insertCharacter(response.results)
            response
        } else {
            CharacterResponse(characterDao.getCharacterList())
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

    suspend fun getCharacterListOffline(): List<CharacterEntity> = characterDao.getCharacterList()

    suspend fun getCharacterByIdOffline(id: Int) = characterDao.getCharacterById(id)
}