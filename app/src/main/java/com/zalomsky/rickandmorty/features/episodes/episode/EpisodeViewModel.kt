package com.zalomsky.rickandmorty.features.episodes.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodesParams
import com.zalomsky.rickandmorty.domain.usecase.episodes.GetAllEpisodesUseCase
import com.zalomsky.rickandmorty.features.episodes.paging.EpisodePageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val getAllEpisodesUseCase: GetAllEpisodesUseCase
): ViewModel() {

    private val _query = MutableStateFlow(EpisodesParams("", ""))
    val query: StateFlow<EpisodesParams> = _query

    val episodesList: Flow<PagingData<EpisodeEntity>> = query.debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { (name, episode) ->
            getFilteredEpisodesList(name, episode)
        }
        .cachedIn(viewModelScope)

    fun updateQueryEpisodes(newQuery: String, newEpisode: String) {
        _query.value = EpisodesParams(newQuery, newEpisode)
    }

    fun resetFilters() {
        _query.value = EpisodesParams("", "")
    }

    private fun getFilteredEpisodesList(name: String, episode: String): Flow<PagingData<EpisodeEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EpisodePageSource(getAllEpisodesUseCase, name, episode) }
        ).flow
    }
}