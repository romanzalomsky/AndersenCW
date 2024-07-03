package com.zalomsky.rickandmorty.domain.usecase.locations

import com.zalomsky.rickandmorty.domain.model.LocationResponse
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository
) {

    suspend fun execute(
        page: Int,
        name: String?,
        type: String?,
        dimension: String?
    ): LocationResponse {
        return locationsRepository.getLocationsList(page, name, type, dimension)
    }
}