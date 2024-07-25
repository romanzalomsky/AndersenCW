package com.zalomsky.rickandmorty.network.api

import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import com.zalomsky.rickandmorty.domain.model.EpisodeEntity
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterApi {

    @GET("character")
    suspend fun getCharactersList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("gender") gender: String?,
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterEntity

    @GET
    suspend fun getEpisode(@Url url: String): EpisodeEntity
}