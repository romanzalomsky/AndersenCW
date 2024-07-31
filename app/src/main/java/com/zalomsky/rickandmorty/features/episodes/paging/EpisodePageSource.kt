package com.zalomsky.rickandmorty.features.episodes.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.usecase.episodes.GetAllEpisodesUseCase

class EpisodePageSource(
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase,
    private val query: String,
    private val episode: String?,
): PagingSource<Int, EpisodeEntity>() {

    override fun getRefreshKey(state: PagingState<Int, EpisodeEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeEntity> {
        return try {
            val page: Int = params.key ?: 1
            val response = getAllEpisodesUseCase.invoke(page, query, episode)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}