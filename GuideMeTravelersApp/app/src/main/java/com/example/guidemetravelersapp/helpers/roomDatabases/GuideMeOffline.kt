package com.example.guidemetravelersapp.helpers.roomDatabases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.roomConverters.AddressTypeConverter
import com.example.guidemetravelersapp.helpers.roomDAO.IAudioguidesDao
import com.example.guidemetravelersapp.helpers.roomDAO.ILocationsDao

@Database(entities = [Location::class, Audioguide::class], version = 1)
@TypeConverters(AddressTypeConverter::class)
abstract class GuideMeOffline: RoomDatabase() {
    abstract fun locatioDao(): ILocationsDao
    abstract fun audioguideDao(): IAudioguidesDao

    companion object {
        @Volatile
        private var INSTANCE: GuideMeOffline? = null

        fun getDatabase(context: Context): GuideMeOffline {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GuideMeOffline::class.java,
                    "guideme_offline"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }

        }
    }
}