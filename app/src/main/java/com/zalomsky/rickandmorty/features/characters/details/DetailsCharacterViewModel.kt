package com.zalomsky.rickandmorty.features.characters.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.repository.CharacterRepository
import com.zalomsky.rickandmorty.domain.usecase.characters.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val characterRepository: CharacterRepository
): ViewModel() {

    private val _episodes = MutableStateFlow<List<EpisodeEntity>>(emptyList())
    val episodes: StateFlow<List<EpisodeEntity>> = _episodes

    private val _character = MutableStateFlow<CharacterEntity?>(null)
    val character: StateFlow<CharacterEntity?> get() = _character

    fun fetchEpisodes(urls: List<String>) {
        viewModelScope.launch {
            val episodes = characterRepository.fetchEpisodes(urls)
            _episodes.value = episodes
        }
    }

    fun fetchCharacterById(id: Int) {
        viewModelScope.launch {
            try {
                val character = getCharacterByIdUseCase(id)
                _character.value = character
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error fetching character by id $id", e)
            }
        }
    }
}