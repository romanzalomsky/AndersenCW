package com.zalomsky.rickandmorty.features.locations.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zalomsky.rickandmorty.domain.model.Locations
import com.zalomsky.rickandmorty.domain.usecase.locations.GetAllLocationsUseCase

class LocationsPageSource(
    private val getAllLocationsUseCase: GetAllLocationsUseCase,
    private val query: String,
    private val type: String?,
    private val dimension: String?,
) : PagingSource<Int, Locations>() {

    override fun getRefreshKey(state: PagingState<Int, Locations>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Locations> {
        return try {
            val page: Int = params.key ?: 1
            val response = getAllLocationsUseCase.execute(page, query, type, dimension)

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}