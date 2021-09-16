package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.services.AuthenticationService
import com.example.guidemetravelersapp.services.ReservationService
import kotlinx.coroutines.launch
import java.lang.Exception

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val reservationService: ReservationService = ReservationService(application)
    private val authService: AuthenticationService = AuthenticationService(application)

    var pastExperienceReservations: ApiResponse<List<ExperienceReservation>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))

    init {
        getPastExperiences()
    }

    private fun getPastExperiences() {
        viewModelScope.launch {
            val userEmail = authService.getCurrentFirebaseUserEmail()
            try {
                val result = userEmail?.let { reservationService.getPastExperiences(it) }
                pastExperienceReservations = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                pastExperienceReservations = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }
}