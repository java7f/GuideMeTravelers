package com.example.guidemetravelersapp.services

import android.content.Context
import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.dataModels.ExperienceReservationRequest
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.interfaces.IReservationService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ReservationService(context: Context) {
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IReservationService::class.java)

    suspend fun getPastExperiences(touristId: String): List<ExperienceReservation> {
        return coroutineScope {
            val getPastReservationsTask = async { apiService.getPastExperiencesForTourist("api/Reservations/getPastReservationsTourist/$touristId").body() }
            getPastReservationsTask.await()!!
        }
    }

    suspend fun insertReservationRequest(experienceReservationRequest: ExperienceReservationRequest) {
        coroutineScope {
            val insertReservationRequestTask = async { apiService.insertReservationRequest("api/Reservations/insertReservationRequest", experienceReservationRequest) }
        }
    }

    suspend fun rateExperience(experienceReservation: ExperienceReservation) {
        coroutineScope {
            val rateExperienceTask = async { apiService.rateExperience("api/Reservations/rateReservation", experienceReservation) }
        }
    }
}