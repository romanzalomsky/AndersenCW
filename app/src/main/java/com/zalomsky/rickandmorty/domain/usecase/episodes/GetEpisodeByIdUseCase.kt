package com.zalomsky.rickandmorty.domain.usecase.episodes

import com.zalomsky.rickandmorty.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetEpisodeByIdUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {

    suspend operator fun invoke(id: Int) = episodeRepository.getEpisodeById(id)
}