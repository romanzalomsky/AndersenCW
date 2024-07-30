package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM character_table")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM character_table WHERE id=:characterId")
    fun getCharacterById(characterId: Int): CharacterEntity
}