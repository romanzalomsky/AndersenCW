package com.zalomsky.rickandmorty.features.characters.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.usecase.characters.GetAllCharactersUseCase

class CharacterPageSource(
    private val charactersUseCase: GetAllCharactersUseCase,
    private val query: String,
    private val status: String?,
    private val species: String?,
    private val gender: String?
) : PagingSource<Int, Character>() {

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val page: Int = params.key ?: 1
            val response = charactersUseCase.execute(page, query, status, species, gender)

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