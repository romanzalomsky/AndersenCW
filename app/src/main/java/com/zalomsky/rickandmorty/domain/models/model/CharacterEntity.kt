package com.zalomsky.rickandmorty.domain.models.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zalomsky.rickandmorty.domain.models.converter.Converters

@Entity(tableName = "character_table")
@TypeConverters(Converters::class)
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) {
    fun toCharacterEntity(): CharacterEntity {
        return CharacterEntity(
            id = this.id,
            name = this.name,
            status = this.status,
            species = this.species,
            type = this.type,
            gender = this.gender,
            origin = this.origin,
            location = this.location,
            image = this.image,
            episode = this.episode,
            url = this.url,
            created = this.created
        )
    }
}

data class Origin(
    val name: String,
    val url: String
)

data class Location(
    val name: String,
    val url: String
)

data class CharacterResponse(
    val results: List<CharacterEntity>
)

data class QueryParams(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = ""
)