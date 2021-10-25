package com.example.guidemetravelersapp.dataModels

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class ExperienceReservationRequest(
    var reservationStatus: Int = ReservationStatus.PENDING.ordinal
) : ReservationBase()

enum class ReservationStatus : JsonSerializer<ReservationStatus> {
    PENDING,
    ACCEPTED,
    REJECTED;

    override fun serialize(
        src: ReservationStatus?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return JsonObject().apply { addProperty(src?.name, src?.ordinal) }
    }
}