package com.zalomsky.rickandmorty.features.characters.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import com.zalomsky.rickandmorty.domain.usecase.characters.GetAllCharactersUseCase
import com.zalomsky.rickandmorty.network.isInternetAvailable

class CharacterPageSource(
    private val getAllCharactersUseCase: GetAllCharactersUseCase,
    private val charactersRepository: CharacterRepository,
    private val query: String,
    private val status: String?,
    private val species: String?,
    private val gender: String?,
    private val context: Context
) : PagingSource<Int, CharacterEntity>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        return try {
            val page: Int = params.key ?: 1

            val characters = if (isInternetAvailable(context)) {
                getAllCharactersUseCase.execute(page, query, status, species, gender)
            } else {
                charactersRepository.getFilteredCharacters(query, status, species, gender)
            }
            LoadResult.Page(
                data = characters,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (characters.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}