package com.zalomsky.rickandmorty.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "episode_table")
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "episodeName") val name: String,
    @ColumnInfo(name = "episodeAirDate") val air_date: String,
    @ColumnInfo(name = "episodeEpisode") val episode: String,
    @ColumnInfo(name = "episodeCharacters") val characters: List<String>,
    @ColumnInfo(name = "episodeUrl") val url: String,
    @ColumnInfo(name = "episodeCreated") val created: String
)

data class EpisodeResponse(
    val results: List<EpisodeEntity>
)

data class EpisodesParams(
    val name: String = "",
    val episode: String = ""
)
