package com.zalomsky.rickandmorty.domain.models.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.Location
import com.zalomsky.rickandmorty.domain.models.model.Origin

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromOrigin(origin: Origin): String {
        return "${origin.name}|${origin.url}"
    }

    @TypeConverter
    fun toOrigin(data: String): Origin {
        val parts = data.split("|")
        return Origin(parts[0], parts[1])
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return "${location.name}|${location.url}"
    }

    @TypeConverter
    fun toLocation(data: String): Location {
        val parts = data.split("|")
        return Location(parts[0], parts[1])
    }
}