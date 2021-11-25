package com.example.guidemetravelersapp.services

import android.content.Context
import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.dataModels.ExperienceReservationRequest
import com.example.guidemetravelersapp.dataModels.GuidingOffer
import com.example.guidemetravelersapp.dataModels.TouristAlert
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

    suspend fun getUpcomingExperiences(touristId: String): List<ExperienceReservation> {
        return coroutineScope {
            val getPastReservationsTask = async { apiService.getUpcomingExperiencesForTourist("api/Reservations/getTouristReservations/$touristId").body() }
            getPastReservationsTask.await()!!
        }
    }

    suspend fun getRequestReservationsForTourist(touristId: String): List<ExperienceReservationRequest> {
        return coroutineScope {
            val getPastReservationsTask = async { apiService.getRequestReservationsForTourist("api/Reservations/requestForTourist/$touristId").body() }
            getPastReservationsTask.await()!!
        }
    }

    suspend fun insertReservationRequest(experienceReservationRequest: ExperienceReservationRequest) {
        coroutineScope {
            val insertReservationRequestTask = async { apiService.insertReservationRequest("api/Reservations/insertReservationRequest", experienceReservationRequest) }
        }
    }

    suspend fun insertTouristAlert(touristAlert: TouristAlert) {
        coroutineScope {
            val insertTouristAlertTask = async { apiService.insertTouristAlert("api/TouristAlert", touristAlert) }
        }
    }

    suspend fun rateExperience(experienceReservation: ExperienceReservation) {
        coroutineScope {
            val rateExperienceTask = async { apiService.rateExperience("api/Reservations/rateReservation", experienceReservation) }
        }
    }

    suspend fun getTouristAlerts(currentUserId: String): List<TouristAlert> {
        return coroutineScope {
            val alertsTask = async { apiService.getTouristAlerts("api/TouristAlert/alerts/$currentUserId").body() }
            alertsTask.await()!!
        }
    }

    suspend fun getGuideOffersForTourist(currentUserId: String): List<GuidingOffer> {
        return coroutineScope {
            val offersTask = async { apiService.getGuideOffersForTourist("api/TouristAlert/guideOffers/$currentUserId").body() }
            offersTask.await()!!
        }
    }

    suspend fun acceptGuideOffer(guideOfferId: String) {
        return  coroutineScope {
            val getReservationTask = async { apiService.acceptGuideOffer("api/TouristAlert/guideOffers/accept/$guideOfferId").body() }
            getReservationTask.await()!!
        }
    }

    suspend fun deleteTouristAlert(touristAlertId: String) {
        return  coroutineScope {
            val getReservationTask = async { apiService.deleteTouristAlert("api/TouristAlert/delete/$touristAlertId").body() }
            getReservationTask.await()!!
        }
    }

    suspend fun rejectGuideOffer(guideOfferId: String) {
        return  coroutineScope {
            val getReservationTask = async { apiService.rejectGuideOffer("api/TouristAlert/guideOffers/reject/$guideOfferId").body() }
            getReservationTask.await()!!
        }
    }
}