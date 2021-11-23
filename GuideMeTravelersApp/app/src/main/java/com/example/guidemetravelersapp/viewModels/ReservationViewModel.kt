package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.dataModels.ExperienceReservationRequest
import com.example.guidemetravelersapp.dataModels.ReservationStatus
import com.example.guidemetravelersapp.dataModels.TouristAlert
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.services.AuthenticationService
import com.example.guidemetravelersapp.services.GuideExperienceService
import com.example.guidemetravelersapp.services.ReservationService
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val reservationService: ReservationService = ReservationService(application)
    private val guideExperienceService: GuideExperienceService = GuideExperienceService(application)
    private val authService: AuthenticationService = AuthenticationService(application)

    var placesClient: PlacesClient = Places.createClient(application)
    var locationSearchValue: String by mutableStateOf("")
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()

    var pastExperienceReservations: ApiResponse<List<ExperienceReservation>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    val currentReservationRequest: ExperienceReservationRequest by mutableStateOf(ExperienceReservationRequest())
    var currentTouristAlert: TouristAlert by mutableStateOf(TouristAlert())
    var initReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = true))
    var newReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var rateReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))

    fun getPastExperiences() {
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

    fun insertReservationRequest(navHostController: NavHostController) {
        viewModelScope.launch {
            try {
                newReservationRequestStatus = ApiResponse(false, true)
                reservationService.insertReservationRequest(currentReservationRequest)
                newReservationRequestStatus = ApiResponse(true, false)
                navHostController.popBackStack()
            }
            catch (e: Exception) {
                newReservationRequestStatus = ApiResponse(false, false, true, e.localizedMessage)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun initCurrentReservationRequest(experienceId: String) {
        viewModelScope.launch {
            try {
                val guideExperienceInfo = guideExperienceService.getExperience(experienceId)
                val currentUserId = authService.getCurrentFirebaseUserId()
                val currentUser = authService.getUserById(currentUserId!!)

                currentReservationRequest.touristUserId = currentUserId
                currentReservationRequest.touristFirstName = currentUser!!.firstName
                currentReservationRequest.touristLastName = currentUser.lastName
                currentReservationRequest.guideExperienceId = experienceId
                currentReservationRequest.guideUserId = guideExperienceInfo.guideFirebaseId
                currentReservationRequest.guideFirstName = guideExperienceInfo.guideFirstName
                currentReservationRequest.guideLastName = guideExperienceInfo.guideLastName
                currentReservationRequest.price = guideExperienceInfo.experiencePrice
                currentReservationRequest.address = guideExperienceInfo.guideAddress
                currentReservationRequest.reservationStatus = ReservationStatus.PENDING.ordinal
                initReservationRequestStatus = ApiResponse(data = true, inProgress = false)
            }
            catch (e: Exception) {
                initReservationRequestStatus = ApiResponse(data = false, inProgress = false, hasError = true, errorMessage = e.localizedMessage)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun initCurrentTouristAlert(experienceId: String) {
        viewModelScope.launch {
            try {
                val currentUserId = authService.getCurrentFirebaseUserId()
                val currentUser = authService.getUserById(currentUserId!!)

                currentTouristAlert = TouristAlert(
                    touristFirstName = "${currentUser!!.firstName} ${currentUser!!.lastName}",
                    touristPhotoUrl = currentUser.profilePhotoUrl,
                    touristCountry = currentUser.country,
                )
                initReservationRequestStatus = ApiResponse(data = true, inProgress = false)
            }
            catch (e: Exception) {
                initReservationRequestStatus = ApiResponse(data = false, inProgress = false, hasError = true, errorMessage = e.localizedMessage)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun rateExperience(experienceReservation: ExperienceReservation) {
        viewModelScope.launch {
            try {
                rateReservationRequestStatus = ApiResponse(false, true)
                reservationService.rateExperience(experienceReservation)
                rateReservationRequestStatus = ApiResponse(true, false)
                pastExperienceReservations = ApiResponse(data = emptyList(), inProgress = true)
                getPastExperiences()
            }
            catch (e: Exception) {
                rateReservationRequestStatus = ApiResponse(true, false)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    fun updateFromDate(newDate: Date) {
        currentReservationRequest.fromDate = newDate
    }

    fun updateToDate(newDate: Date) {
        currentReservationRequest.toDate = newDate
    }

    fun onQueryChanged(inputText: String) {
        locationSearchValue = inputText
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setQuery(inputText)
                .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                predictions = mutableListOf()
                for (prediction in response.autocompletePredictions) {
                    predictions.add(prediction)
                }
            }
    }

    fun onPlaceItemSelected(placeItem: AutocompletePrediction) {
        locationSearchValue = placeItem.getPrimaryText(null).toString()
        predictions = mutableListOf()
        currentTouristAlert.touristDestination = locationSearchValue
    }
}