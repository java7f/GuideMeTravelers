package com.example.guidemetravelersapp.dataModels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.guidemetravelersapp.helpers.utils.Utils

@Entity(tableName = Utils.LOCATIONS_TABLE)
data class Location(
    @PrimaryKey var id: String = "",
    @ColumnInfo var name: String = "",
    @ColumnInfo var userId: String = "",
    @Ignore var locationPhotoUrl: String = "",
    @ColumnInfo var address: Address = Address()
)