package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.LocationResponse
import com.zalomsky.rickandmorty.network.api.LocationsApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val locationsApi: LocationsApi
) {

    suspend fun getLocationsList(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): LocationResponse {
        return locationsApi.getLocationsList(page, name, type, dimension)
    }

    suspend fun fetchCharacters(residents: List<String>): List<CharacterEntity> = coroutineScope {
        val deferredCharacters = residents.map { residents ->
            async {
                locationsApi.getCharacter(residents)
            }
        }
        deferredCharacters.awaitAll()
    }

    suspend fun getLocationById(id: Int) = locationsApi.getLocationById(id)
}