package com.example.guidemetravelersapp.dataModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.guidemetravelersapp.helpers.utils.Utils

@Entity(tableName = Utils.AUDIOGUIDES_TABLE)
data class Audioguide(
    @PrimaryKey var id: String = "",
    @ColumnInfo var name: String = "",
    @ColumnInfo var locationId: String = "",
    @ColumnInfo(name = Utils.LOCAL_URI) var audioguideUrl: String = "",
    @ColumnInfo(defaultValue = "en") var audioLocale: String = "",
    @ColumnInfo var audiofileName: String = "",
    @ColumnInfo var macAddress: String = "",
    @ColumnInfo var isDownloaded: Boolean = false,
)