package com.zalomsky.rickandmorty.domain.model

data class Episode(
    val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)

data class EpisodeResponse(
    val results: List<Episode>
)

data class EpisodesParams(
    val name: String = "",
    val episode: String = ""
)
