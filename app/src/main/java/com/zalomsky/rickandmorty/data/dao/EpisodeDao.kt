package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.model.EpisodeEntity

@Dao
interface EpisodeDao {

    @Insert
    fun insertEpisode(episodeEntity: EpisodeEntity)

    @Query("SELECT * FROM episode_table")
    fun getEpisodeList(): List<EpisodeEntity>

    @Query("SELECT * FROM episode_table WHERE id=:episodeId")
    fun getEpisodeById(episodeId: Int): EpisodeEntity
}