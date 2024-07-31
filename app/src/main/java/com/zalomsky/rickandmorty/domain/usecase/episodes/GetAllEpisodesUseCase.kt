package com.zalomsky.rickandmorty.domain.usecase.episodes

import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetAllEpisodesUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository
) {

    suspend operator fun invoke(page: Int, name: String?, episode: String?): List<EpisodeEntity> {
        return episodeRepository.getEpisodeList(page, name, episode)
    }

}