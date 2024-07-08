package com.zalomsky.rickandmorty.features.episodes.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zalomsky.rickandmorty.domain.model.Episode
import com.zalomsky.rickandmorty.domain.usecase.episodes.GetEpisodeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class DetailsEpisodeViewModel @Inject constructor(
    private val getEpisodeByIdUseCase: GetEpisodeByIdUseCase
): ViewModel() {

    fun getEpisodeById(id: Int): Flow<Episode> {
        return flow {
            val episode = getEpisodeByIdUseCase(id)
            emit(episode)
        }.catch { e ->
            Log.e("DetailsEpisodeViewModel", "Error getting episode: ${e.message}")
        }.flowOn(Dispatchers.IO)
    }
}