package com.example.guidemetravelersapp.dataModels

import java.util.*

data class GuidingOffer(
    var id: String = "",
    var touristId: String = "",
    var guideId: String = "",
    var guideExperienceId: String = "",
    var guidePhotoUrl: String = "",
    var guideFirstName: String = "",
    var guideLastName: String = "",
    var touristFirstName: String = "",
    var touristLastName: String = "",
    var touristDestination: String = "",
    var touristAlertId: String = "",
    var reservationStatus: Int = ReservationStatus.PENDING.ordinal,
    var fromDate: Date = Date(),
    var toDate: Date = Date(),
)