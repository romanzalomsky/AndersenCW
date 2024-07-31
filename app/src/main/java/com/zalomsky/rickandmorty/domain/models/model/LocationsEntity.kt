package com.zalomsky.rickandmorty.domain.models.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zalomsky.rickandmorty.domain.models.converter.Converters

@Entity(tableName = "location_table")
@TypeConverters(Converters::class)
data class LocationsEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
) {
    fun toLocationEntity(): LocationsEntity{
        return LocationsEntity(
            id = this.id,
            name = this.name,
            type = this.type,
            dimension = this.dimension,
            residents = this.residents,
            url = this.url,
            created = this.created
        )
    }
}

data class LocationResponse(
    val results: List<LocationsEntity>
)

data class LocationsParams(
    val name: String = "",
    val type: String = "",
    val dimension: String = ""
)
