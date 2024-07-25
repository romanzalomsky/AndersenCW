package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.model.CharacterEntity

@Dao
interface CharacterDao {

    @Insert
    fun insertCharacter(characterEntities: List<CharacterEntity>)

    @Query("SELECT * FROM character_table")
    fun getCharacterList(): List<CharacterEntity>

    @Query("SELECT * FROM character_table WHERE id=:characterId")
    fun getCharacterById(characterId: Int): CharacterEntity
}