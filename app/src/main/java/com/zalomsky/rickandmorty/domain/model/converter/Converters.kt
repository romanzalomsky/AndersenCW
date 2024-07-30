package com.zalomsky.rickandmorty.domain.model.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zalomsky.rickandmorty.domain.model.Location
import com.zalomsky.rickandmorty.domain.model.Origin

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromOrigin(origin: Origin): String {
        return Gson().toJson(origin)
    }

    @TypeConverter
    fun toOrigin(data: String): Origin {
        return Gson().fromJson(data, Origin::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun toLocation(data: String): Location {
        return Gson().fromJson(data, Location::class.java)
    }
}