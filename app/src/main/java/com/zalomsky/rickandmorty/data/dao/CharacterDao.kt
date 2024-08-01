package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM character_table")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM character_table WHERE id=:characterId")
    suspend fun getCharacterById(characterId: Int): CharacterEntity

/*    @Query("SELECT * FROM character_table WHERE url IN (:urls)")
    suspend fun getEpisodesByUrls(urls: List<String>): List<EpisodeEntity>*/

    @Query("SELECT * FROM character_table WHERE " +
            "name LIKE '%' || :name || '%' " +
            "AND status LIKE '%' || :status || '%' " +
            "AND species LIKE '%' || :species || '%' " +
            "AND gender LIKE '%' || :gender || '%'")
    suspend fun getFilteredCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): List<CharacterEntity>
}