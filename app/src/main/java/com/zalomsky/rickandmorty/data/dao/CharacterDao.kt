package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM character_table")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM character_table WHERE id=:characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity

    @Query("""
        SELECT * FROM character_table 
        WHERE (:name IS NULL OR name LIKE '%' || :name || '%') 
        AND (:status IS NULL OR status = :status) 
        AND (:species IS NULL OR species = :species) 
        AND (:gender IS NULL OR gender = :gender)
    """)
    suspend fun getFilteredCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): List<CharacterEntity>
}