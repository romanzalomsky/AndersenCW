package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.models.model.CharacterEntity
import com.zalomsky.rickandmorty.domain.models.model.LocationsEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<LocationsEntity>)

    @Query("SELECT * FROM location_table")
    suspend fun getAllLocations(): List<LocationsEntity>

    @Query("SELECT * FROM location_table WHERE id=:locationId")
    suspend fun getLocationById(locationId: Int): LocationsEntity

    @Query("SELECT * FROM character_table WHERE url IN (:residents)")
    suspend fun getResidents(residents: List<String>): List<CharacterEntity>

    @Query("SELECT * FROM location_table WHERE " +
            "name LIKE '%' || :name || '%' " +
            "AND type LIKE '%' || :type || '%' " +
            "AND dimension LIKE '%' || :dimension || '%' ")
    suspend fun getFilteredLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): List<LocationsEntity>
}