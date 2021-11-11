package com.example.guidemetravelersapp.dataModels

import com.example.guidemetravelersapp.helpers.models.Coordinate

data class Address(
    var city: String = "",
    var country: String = "",
    var coordinates: Coordinate = Coordinate(),
)