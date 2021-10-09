package com.example.guidemetravelersapp.dataModels

import com.example.guidemetravelersapp.helpers.models.Coordinate

data class Location(
    var id: String = "",
    var name: String = "",
    var userId: String = "",
    var locationPhotoUrl: String = "",
    var coordinates: Coordinate = Coordinate()
)