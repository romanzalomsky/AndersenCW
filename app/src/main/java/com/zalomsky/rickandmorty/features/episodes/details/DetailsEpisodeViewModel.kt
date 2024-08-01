package com.zalomsky.rickandmorty.features.episodes.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.repository.EpisodeRepository
import com.zalomsky.rickandmorty.domain.usecase.episodes.GetEpisodeByIdUseCase
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
class DetailsEpisodeViewModel @Inject constructor(
    private val getEpisodeByIdUseCase: GetEpisodeByIdUseCase,
    private val episodeRepository: EpisodeRepository
): ViewModel() {

    private val _characters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val characters: StateFlow<List<CharacterEntity>> = _characters

    private val _episode = MutableStateFlow<EpisodeEntity?>(null)
    val episode: StateFlow<EpisodeEntity?> get() = _episode

    fun fetchCharacters(characters: List<String>) {
        viewModelScope.launch {
            val characters = episodeRepository.fetchCharacters(characters)
            _characters.value = characters
        }
    }

    fun fetchEpisodeById(id: Int) {
        viewModelScope.launch {
            try {
                val episode = getEpisodeByIdUseCase(id)
                _episode.value = episode
            } catch (e: Exception) {
                Log.e("EpisodeViewModel", "Error fetching episode by id $id", e)
            }
        }
    }
}