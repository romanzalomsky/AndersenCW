package com.zalomsky.rickandmorty.domain.usecase.locations

import com.zalomsky.rickandmorty.domain.models.model.LocationResponse
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity
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
    ): List<LocationsEntity> {
        return locationsRepository.getLocationsList(page, name, type, dimension)
    }
}