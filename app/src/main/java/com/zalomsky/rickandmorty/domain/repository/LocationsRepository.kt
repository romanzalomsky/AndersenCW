package com.zalomsky.rickandmorty.domain.repository

import com.zalomsky.rickandmorty.domain.model.LocationResponse
import com.zalomsky.rickandmorty.network.api.LocationsApi
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

    suspend fun getLocationById(id: Int) = locationsApi.getLocationById(id)
}