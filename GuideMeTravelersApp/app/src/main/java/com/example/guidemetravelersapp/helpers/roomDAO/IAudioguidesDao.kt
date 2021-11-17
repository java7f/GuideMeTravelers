package com.example.guidemetravelersapp.helpers.roomDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guidemetravelersapp.dataModels.Audioguide

@Dao
interface IAudioguidesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioguide(vararg audioguides: Audioguide)

    @Query("SELECT * FROM audioguides WHERE locationId = :locationId")
    suspend fun getAudioguidesForLocation(locationId: String): List<Audioguide>

    @Query("SELECT * FROM audioguides WHERE id = :audioguideId")
    suspend fun getAudioguide(audioguideId: String): Audioguide

}