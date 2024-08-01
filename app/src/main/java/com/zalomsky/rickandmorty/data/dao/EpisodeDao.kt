package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.EpisodeEntity

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeEntity>)

    @Query("SELECT * FROM episode_table")
    suspend fun getAllEpisodes(): List<EpisodeEntity>

    @Query("SELECT * FROM episode_table WHERE id=:episodeId")
    suspend fun getEpisodeById(episodeId: Int): EpisodeEntity

    @Query("SELECT * FROM episode_table WHERE " +
            "name LIKE '%' || :name || '%' " +
            "AND episode LIKE '%' || :episode || '%' ")
    suspend fun getFilteredEpisodes(
        name: String?,
        episode: String?
    ): List<EpisodeEntity>
}