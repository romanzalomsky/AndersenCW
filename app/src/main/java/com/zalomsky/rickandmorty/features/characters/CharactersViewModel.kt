package com.zalomsky.rickandmorty.features.characters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zalomsky.rickandmorty.domain.model.Character
import com.zalomsky.rickandmorty.domain.model.QueryParams
import com.zalomsky.rickandmorty.domain.usecase.GetAllCharactersUseCase
import com.zalomsky.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import com.zalomsky.rickandmorty.network.CharacterPageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersUseCase: GetAllCharactersUseCase,
    private val characterUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _query = MutableStateFlow(QueryParams("", "", "", ""))
    val query: StateFlow<QueryParams> = _query

    val charactersList: Flow<PagingData<Character>> = query.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { (name, status, species, gender) ->
            getFilteredCharactersList(name, status, species, gender)
        }
        .cachedIn(viewModelScope)

    fun updateQuery(newQuery: String, newStatus: String, newSpecies: String, newGender: String) {
        _query.value = QueryParams(newQuery, newStatus, newSpecies, newGender)
    }

    private fun getFilteredCharactersList(name: String, status: String, species: String, gender: String): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPageSource(charactersUseCase, name, status, species, gender) }
        ).flow
    }

    private val _charactersById = MutableLiveData<Character>()
    val charactersById: LiveData<Character>
        get() = _charactersById

    fun getCharacterById(id: Int) {
        viewModelScope.launch {
            try {
                val character = characterUseCase(id)
                _charactersById.postValue(character)
            } catch (e: Exception) {
                Log.e("asdfghjk", "Exception during request -> ${e.localizedMessage}")
            }
        }
    }
}