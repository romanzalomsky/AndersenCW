package com.zalomsky.rickandmorty.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "character_table")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "characterName") val name: String,
    @ColumnInfo(name = "characterStatus") val status: String,
    @ColumnInfo(name = "characterSpecies") val species: String,
    @ColumnInfo(name = "characterType") val type: String,
    @ColumnInfo(name = "characterGender") val gender: String,
    @ColumnInfo(name = "characterOrigin") val origin: Origin,
    @ColumnInfo(name = "characterLocation") val location: Location,
    @ColumnInfo(name = "characterImage") val image: String,
    @ColumnInfo(name = "characterEpisode") val episode: List<String>,
    @ColumnInfo(name = "characterUrl") val url: String,
    @ColumnInfo(name = "characterCreated") val created: String
)

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