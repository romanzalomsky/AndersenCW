package com.zalomsky.rickandmorty.features.characters.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.QueryParams
import com.zalomsky.rickandmorty.domain.usecase.characters.GetAllCharactersUseCase
import com.zalomsky.rickandmorty.features.characters.paging.CharacterPageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUseCase: GetAllCharactersUseCase
) : ViewModel() {

    private val _query = MutableStateFlow(QueryParams("", "", "", ""))
    val query: StateFlow<QueryParams> = _query

    val charactersList: Flow<PagingData<CharacterEntity>> = query.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { (name, status, species, gender) ->
            getFilteredCharactersList(name, status, species, gender)
        }
        .cachedIn(viewModelScope)

    fun updateQuery(newQuery: String, newStatus: String, newSpecies: String, newGender: String) {
        _query.value = QueryParams(newQuery, newStatus, newSpecies, newGender)
    }

    fun resetFilters() {
        _query.value = QueryParams("", "", "", "")
    }

    private fun getFilteredCharactersList(name: String, status: String, species: String, gender: String): Flow<PagingData<CharacterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPageSource(charactersUseCase, name, status, species, gender) }
        ).flow
    }
}