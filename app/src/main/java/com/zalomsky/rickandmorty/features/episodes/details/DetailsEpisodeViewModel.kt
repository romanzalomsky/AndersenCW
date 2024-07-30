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

    fun fetchCharacters(characters: List<String>) {
        viewModelScope.launch {
            val characters = episodeRepository.fetchCharacters(characters)
            _characters.value = characters
        }
    }

    fun getEpisodeById(id: Int): Flow<EpisodeEntity> {
        return flow {
            val episode = getEpisodeByIdUseCase(id)
            emit(episode)
        }.catch { e ->
            Log.e("DetailsEpisodeViewModel", "Error getting episode: ${e.message}")
        }.flowOn(Dispatchers.IO)
    }
}