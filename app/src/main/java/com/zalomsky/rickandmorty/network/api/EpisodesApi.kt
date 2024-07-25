package com.zalomsky.rickandmorty.network.api

import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.model.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface EpisodesApi {

    @GET("episode")
    suspend fun getEpisodesList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?,
    ): EpisodeResponse

    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): EpisodeEntity

    @GET
    suspend fun getCharacter(@Url characters: String): CharacterEntity
}