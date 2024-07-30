package com.zalomsky.rickandmorty.network.api

import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.LocationResponse
import com.zalomsky.rickandmorty.domain.model.Locations
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface LocationsApi {

    @GET("location")
    suspend fun getLocationsList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?
    ): LocationResponse

    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): Locations

    @GET
    suspend fun getCharacter(@Url residents: String): CharacterEntity
}