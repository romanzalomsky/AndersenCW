package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.EpisodeResponse
import com.zalomsky.rickandmorty.network.api.EpisodesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val episodesApi: EpisodesApi
) {

    suspend fun getEpisodeList(
        page: Int,
        name: String?,
        episode: String?
    ): EpisodeResponse{
        return episodesApi.getEpisodesList(page, name, episode)
    }

    suspend fun fetchCharacters(characters: List<String>): List<Character> = coroutineScope {
        val deferredCharacters = characters.map { characters ->
            async {
                episodesApi.getCharacter(characters)
            }
        }
        deferredCharacters.awaitAll()
    }

    suspend fun getEpisodeById(id: Int) = episodesApi.getEpisodeById(id)
}