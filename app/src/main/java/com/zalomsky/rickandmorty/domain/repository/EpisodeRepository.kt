package com.zalomsky.rickandmorty.domain.repository

import android.content.Context
import com.zalomsky.rickandmorty.data.dao.EpisodeDao
import com.zalomsky.rickandmorty.data.dao.LocationDao
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeResponse
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity
import com.zalomsky.rickandmorty.network.api.EpisodesApi
import com.zalomsky.rickandmorty.network.isInternetAvailable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val episodesApi: EpisodesApi,
    private val episodeDao: EpisodeDao,
    private val context: Context
) {

    suspend fun getEpisodeList(
        page: Int,
        name: String?,
        episode: String?
    ): List<EpisodeEntity> {
        return if (isInternetAvailable(context)) {
            val response = episodesApi.getEpisodesList(page, name, episode)
            val episodes = response.results.map { it.toEpisodeEntity() }
            episodeDao.insertEpisodes(episodes)
            episodes
        } else {
            episodeDao.getAllEpisodes()
        }
    }

    suspend fun fetchCharacters(characters: List<String>): List<CharacterEntity> = coroutineScope {
        val deferredCharacters = characters.map { characters ->
            async {
                episodesApi.getCharacter(characters)
            }
        }
        deferredCharacters.awaitAll()
    }

    suspend fun getEpisodeById(id: Int): EpisodeEntity {
        return if (isInternetAvailable(context)) {
            val episode = episodesApi.getEpisodeById(id)
            episodeDao.insertEpisodes(listOf(episode.toEpisodeEntity()))
            episode.toEpisodeEntity()
        } else {
            episodeDao.getEpisodeById(id)
        }
    }
}