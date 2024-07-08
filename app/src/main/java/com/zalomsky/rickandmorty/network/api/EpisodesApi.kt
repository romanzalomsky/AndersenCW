package com.zalomsky.rickandmorty.network.api

import com.zalomsky.rickandmorty.domain.model.Episode
import com.zalomsky.rickandmorty.domain.model.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApi {

    @GET("episode")
    suspend fun getEpisodesList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?,
    ): EpisodeResponse

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): Episode
}