package com.zalomsky.rickandmorty.features.locations.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zalomsky.rickandmorty.domain.model.Locations
import com.zalomsky.rickandmorty.domain.usecase.locations.GetLocationByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class DetailsLocationViewModel @Inject constructor(
    private val getLocationByIdUseCase: GetLocationByIdUseCase
): ViewModel() {

    fun getLocationById(id: Int): Flow<Locations> {
        return flow {
            val location = getLocationByIdUseCase(id)
            emit(location)
        }.catch { e ->
            Log.e("DetailsLocationViewModel", "Error getting character: ${e.message}")
        }.flowOn(Dispatchers.IO)
    }
}