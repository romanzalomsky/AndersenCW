package com.zalomsky.rickandmorty.domain.usecase.locations

import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import javax.inject.Inject

class GetLocationByIdUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository
) {

    suspend operator fun invoke(id: Int): LocationsEntity {
        return locationsRepository.getLocationById(id)
    }
}