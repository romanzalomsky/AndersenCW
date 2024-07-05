package com.zalomsky.rickandmorty.domain.model

data class Locations(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)

data class LocationResponse(
    val results: List<Locations>
)

data class LocationsParams(
    val name: String = "",
    val type: String = "",
    val dimension: String = ""
)
