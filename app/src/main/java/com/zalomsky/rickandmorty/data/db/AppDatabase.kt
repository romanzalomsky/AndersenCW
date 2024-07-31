package com.zalomsky.rickandmorty.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.data.dao.EpisodeDao
import com.zalomsky.rickandmorty.data.dao.LocationDao
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.converter.Converters
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity

@Database(
    entities = [CharacterEntity::class, LocationsEntity::class, EpisodeEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun locationDao(): LocationDao

    abstract fun episodeDao(): EpisodeDao
}