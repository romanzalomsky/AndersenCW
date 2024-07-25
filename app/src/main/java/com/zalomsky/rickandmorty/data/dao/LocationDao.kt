package com.zalomsky.rickandmorty.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalomsky.rickandmorty.domain.model.Location

@Dao
interface LocationDao {

    @Insert
    fun insertLocation(location: Location)

    @Query("SELECT * FROM location_table")
    fun getLocationList(): List<Location>

    @Query("SELECT * FROM location_table WHERE id=:locationId")
    fun getLocationById(locationId: Int): Location
}