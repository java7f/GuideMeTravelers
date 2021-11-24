package com.example.guidemetravelersapp.dataModels

import java.util.*

data class TouristAlert(
    var id: String = "",
    var touristFirstName: String = "",
    var touristPhotoUrl: String = "",
    var touristCountry: String = "",
    var touristDestination: String = "",
    var alertComment: String = "",
    var fromDate: Date = Date(),
    var toDate: Date = Date(),
    var touristLanguages: List<String> = emptyList(),
    var experienceTags: List<String> = emptyList()
)