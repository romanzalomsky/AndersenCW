package com.zalomsky.rickandmorty.api

import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterApi {

    @GET("character")
    suspend fun getCharactersList(): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): Character


}