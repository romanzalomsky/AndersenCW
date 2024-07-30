package com.zalomsky.rickandmorty.domain.models.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location_table")
data class LocationsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "locationName") val name: String,
    @ColumnInfo(name = "locationType") val type: String,
    @ColumnInfo(name = "locationDimension") val dimension: String,
    @ColumnInfo(name = "locationResidents") val residents: List<String>,
    @ColumnInfo(name = "locationUrl") val url: String,
    @ColumnInfo(name = "locationCreated") val created: String
)

data class LocationResponse(
    val results: List<LocationsEntity>
)

data class LocationsParams(
    val name: String = "",
    val type: String = "",
    val dimension: String = ""
)
