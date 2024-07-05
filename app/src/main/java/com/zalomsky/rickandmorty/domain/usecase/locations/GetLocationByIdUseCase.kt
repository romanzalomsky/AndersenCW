package com.zalomsky.rickandmorty.domain.usecase.locations

import com.zalomsky.rickandmorty.domain.model.Locations
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import javax.inject.Inject

class GetLocationByIdUseCase @Inject constructor(
    private val locationsRepository: LocationsRepository
) {

    suspend operator fun invoke(id: Int): Locations = locationsRepository.getLocationById(id)
}