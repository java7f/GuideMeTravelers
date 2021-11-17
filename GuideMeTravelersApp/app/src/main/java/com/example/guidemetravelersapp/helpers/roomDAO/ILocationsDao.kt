package com.example.guidemetravelersapp.helpers.roomDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guidemetravelersapp.dataModels.Location

@Dao
interface ILocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(vararg location: Location)

    @Query("SELECT * FROM locations")
    suspend fun getLocations(): List<Location>

    @Query("SELECT * FROM locations WHERE id = :locationId")
    suspend fun getLocation(locationId: String): Location
}