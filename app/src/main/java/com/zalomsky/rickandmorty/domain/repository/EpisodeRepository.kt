package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.domain.model.EpisodeResponse
import com.zalomsky.rickandmorty.network.api.EpisodesApi
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

    suspend fun getEpisodeById(id: Int) = episodesApi.getEpisodeById(id)
}