package com.zalomsky.rickandmorty.network.api

import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getCharacterById(@Path("id") id: Int): Character
}