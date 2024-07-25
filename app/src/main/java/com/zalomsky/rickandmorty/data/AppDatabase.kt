package com.zalomsky.rickandmorty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.data.dao.EpisodeDao
import com.zalomsky.rickandmorty.data.dao.LocationDao
import com.zalomsky.rickandmorty.domain.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.model.EpisodeEntity
import com.zalomsky.rickandmorty.domain.model.LocationsEntity

@Database(entities = [
    CharacterEntity::class,
    EpisodeEntity::class,
    LocationsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun locationDao(): LocationDao

    abstract fun episodeDao(): EpisodeDao
}