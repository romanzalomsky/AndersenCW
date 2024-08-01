package com.zalomsky.rickandmorty.features.locations.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import com.zalomsky.rickandmorty.domain.repository.LocationsRepository
import com.zalomsky.rickandmorty.domain.usecase.locations.GetAllLocationsUseCase
import com.zalomsky.rickandmorty.network.isInternetAvailable

class LocationsPageSource(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val locationsRepository: LocationsRepository,
    private val query: String,
    private val type: String?,
    private val dimension: String?,
    private val context: Context
) : PagingSource<Int, LocationsEntity>() {

    override fun getRefreshKey(state: PagingState<Int, LocationsEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationsEntity> {
        return try {
            val page: Int = params.key ?: 1
            val locations = if (isInternetAvailable(context)) {
                getAllLocationsUseCase.execute(page, query, type, dimension)
            } else {
                locationsRepository.getFilteredLocations(query, type, dimension)
            }
            LoadResult.Page(
                data = locations,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (locations.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}