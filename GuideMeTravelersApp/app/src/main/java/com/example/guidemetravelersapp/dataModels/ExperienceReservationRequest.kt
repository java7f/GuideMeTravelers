package com.example.guidemetravelersapp.dataModels

data class ExperienceReservationRequest(
    var reservationStatus: ReservationStatus
) : ReservationBase()

enum class ReservationStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}