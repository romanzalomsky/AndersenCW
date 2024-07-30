package com.zalomsky.rickandmorty.domain.models.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
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