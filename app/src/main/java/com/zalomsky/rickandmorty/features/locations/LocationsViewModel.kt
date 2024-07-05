package com.zalomsky.rickandmorty.features.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zalomsky.rickandmorty.domain.model.Locations
import com.zalomsky.rickandmorty.domain.model.LocationsParams
import com.zalomsky.rickandmorty.domain.usecase.locations.GetAllLocationsUseCase
import com.zalomsky.rickandmorty.domain.usecase.locations.GetLocationByIdUseCase
import com.zalomsky.rickandmorty.features.locations.paging.LocationsPageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val getLocationByIdUseCase: GetLocationByIdUseCase
): ViewModel() {

    private val _query = MutableStateFlow(LocationsParams("", "", ""))
    val query: StateFlow<LocationsParams> = _query

    val locationsList: Flow<PagingData<Locations>> = query.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { (name, type, dimension) ->
            getFilteredLocationsList(name, type, dimension)
        }
        .cachedIn(viewModelScope)

    fun updateQueryLocations(newQuery: String, newType: String, newDimension: String) {
        _query.value = LocationsParams(newQuery, newType, newDimension)
    }

    fun resetFilters() {
        _query.value = LocationsParams("", "", "")
    }

    private fun getFilteredLocationsList(name: String, type: String, dimension: String): Flow<PagingData<Locations>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LocationsPageSource(getAllLocationsUseCase, name, type, dimension) }
        ).flow
    }
}