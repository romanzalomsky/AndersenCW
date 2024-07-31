package com.zalomsky.rickandmorty.domain.repository

import android.content.Context
import com.zalomsky.rickandmorty.data.dao.LocationDao
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.LocationResponse
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity
import com.zalomsky.rickandmorty.network.api.LocationsApi
import com.zalomsky.rickandmorty.network.isInternetAvailable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val locationsApi: LocationsApi,
    private val locationDao: LocationDao,
    private val context: Context
) {

    suspend fun getLocationsList(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): List<LocationsEntity> {
        return if (isInternetAvailable(context)) {
            val response = locationsApi.getLocationsList(page, name, type, dimension)
            val locations = response.results.map { it.toLocationEntity() }
            locationDao.insertLocations(locations)
            locations
        } else {
            locationDao.getAllLocations()
        }
    }

    suspend fun fetchCharacters(residents: List<String>): List<CharacterEntity> = coroutineScope {
        val deferredCharacters = residents.map { residents ->
            async {
                locationsApi.getCharacter(residents)
            }
        }
        deferredCharacters.awaitAll()
    }

    suspend fun getLocationById(id: Int): LocationsEntity {
        return if (isInternetAvailable(context)) {
            val location = locationsApi.getLocationById(id)
            locationDao.insertLocations(listOf(location.toLocationEntity()))
            location.toLocationEntity()
        } else {
            locationDao.getLocationById(id)
        }
    }
}