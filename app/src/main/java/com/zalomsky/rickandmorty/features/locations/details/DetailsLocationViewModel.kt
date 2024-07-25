package com.zalomsky.rickandmorty.features.locations.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.LocationsEntity
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import com.zalomsky.rickandmorty.domain.usecase.locations.GetLocationByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsLocationViewModel @Inject constructor(
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
    private val locationsRepository: LocationsRepository
): ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters

    fun fetchCharacters(residents: List<String>) {
        viewModelScope.launch {
            val characters = locationsRepository.fetchCharacters(residents)
            _characters.value = characters
        }
    }

    fun getLocationById(id: Int): Flow<LocationsEntity> {
        return flow {
            val location = getLocationByIdUseCase(id)
            emit(location)
        }.catch { e ->
            Log.e("DetailsLocationViewModel", "Error getting character: ${e.message}")
        }.flowOn(Dispatchers.IO)
    }
}