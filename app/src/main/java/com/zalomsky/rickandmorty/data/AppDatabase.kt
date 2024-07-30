package com.zalomsky.rickandmorty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zalomsky.rickandmorty.data.dao.CharacterDao
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.converters.Converters

@Database(entities = [
    CharacterEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}