package com.zalomsky.rickandmorty.domain.models.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zalomsky.rickandmorty.domain.models.converter.Converters

@Entity(tableName = "episode_table")
@TypeConverters(Converters::class)
data class EpisodeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
) {
    fun toEpisodeEntity(): EpisodeEntity {
        return EpisodeEntity(
            id = this.id,
            name = this.name,
            air_date = this.air_date,
            episode = this.episode,
            characters = this.characters,
            url = this.url,
            created = this.created
        )
    }
}

data class EpisodeResponse(
    val results: List<EpisodeEntity>
)

data class EpisodesParams(
    val name: String = "",
    val episode: String = ""
)
