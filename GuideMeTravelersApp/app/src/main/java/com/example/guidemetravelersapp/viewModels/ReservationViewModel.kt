package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.*
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.helpers.pushNotifications.FirebaseNotificationMessagingService
import com.example.guidemetravelersapp.services.AuthenticationService
import com.example.guidemetravelersapp.services.GuideExperienceService
import com.example.guidemetravelersapp.services.ReservationService
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val reservationService: ReservationService = ReservationService(application)
    private val guideExperienceService: GuideExperienceService = GuideExperienceService(application)
    private val authService: AuthenticationService = AuthenticationService(application)
    private val _isRefreshingUpcomingExperiences = MutableStateFlow(false)
    private val _isRefreshingReservationRequests = MutableStateFlow(false)
    private val _isRefreshingPastExperiences = MutableStateFlow(false)
    private val _isRefreshingGuideOffers = MutableStateFlow(false)
    private val _isRefreshingTouristAlerts = MutableStateFlow(false)

    var placesClient: PlacesClient = Places.createClient(application)
    var locationSearchValue: String by mutableStateOf("")
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()

    var pastExperienceReservations: ApiResponse<List<ExperienceReservation>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var upcomingExperienceReservations: ApiResponse<List<ExperienceReservation>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var touristReservationRequests: ApiResponse<List<ExperienceReservationRequest>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    val currentReservationRequest: ExperienceReservationRequest by mutableStateOf(ExperienceReservationRequest())
    var currentTouristAlert: TouristAlert by mutableStateOf(TouristAlert())
    var initReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = true))
    var initTourisAlertStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = true))
    var newReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var newTouristAlertStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var rateReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))

    var touristAlerts: ApiResponse<List<TouristAlert>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var guideOffersForTourist: ApiResponse<List<GuidingOffer>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))

    var acceptGuideOffer: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var rejectGuideOffer: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var deleteTouristAlertStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))

    init {
        initCurrentTouristAlert()
    }

    val isRefreshingUpcomingExperiences: StateFlow<Boolean>
        get() = _isRefreshingUpcomingExperiences.asStateFlow()

    val isRefreshingReservationRequests: StateFlow<Boolean>
        get() = _isRefreshingReservationRequests.asStateFlow()

    val isRefreshingPastExperiences: StateFlow<Boolean>
        get() = _isRefreshingPastExperiences.asStateFlow()

    val isRefreshingGuideOffers: StateFlow<Boolean>
        get() = _isRefreshingGuideOffers.asStateFlow()

    val isRefreshingTouristAlerts: StateFlow<Boolean>
        get() = _isRefreshingTouristAlerts.asStateFlow()

    fun refreshUpcomingExperiences() {
        viewModelScope.launch {
            _isRefreshingUpcomingExperiences.emit(true)
            getUpcomingExperiences()
            _isRefreshingUpcomingExperiences.emit(false)
        }
    }

    fun refreshReservationRequests() {
        viewModelScope.launch {
            _isRefreshingReservationRequests.emit(true)
            getRequestReservationsForTourist()
            _isRefreshingReservationRequests.emit(false)
        }
    }

    fun refreshPastExperiences() {
        viewModelScope.launch {
            _isRefreshingPastExperiences.emit(true)
            getPastExperiences()
            _isRefreshingPastExperiences.emit(false)
        }
    }

    fun refreshGuideOffers() {
        viewModelScope.launch {
            _isRefreshingGuideOffers.emit(true)
            getGuideOffersForTourist()
            _isRefreshingGuideOffers.emit(false)
        }
    }

    fun refreshTouristAlert() {
        viewModelScope.launch {
            _isRefreshingTouristAlerts.emit(true)
            getTouristAlerts()
            _isRefreshingTouristAlerts.emit(false)
        }
    }

    fun getPastExperiences() {
        viewModelScope.launch {
            val currentTouristId = authService.getCurrentFirebaseUserId()
            try {
                val result = currentTouristId?.let { reservationService.getPastExperiences(it) }
                pastExperienceReservations = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                pastExperienceReservations = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun getUpcomingExperiences() {
        viewModelScope.launch {
            val currentTouristId = authService.getCurrentFirebaseUserId()
            try {
                val result = currentTouristId?.let { reservationService.getUpcomingExperiences(it) }
                upcomingExperienceReservations = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                upcomingExperienceReservations = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun getRequestReservationsForTourist() {
        viewModelScope.launch {
            val currentTouristId = authService.getCurrentFirebaseUserId()
            try {
                val result = currentTouristId?.let { reservationService.getRequestReservationsForTourist(it) }
                touristReservationRequests = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                touristReservationRequests = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun getTouristAlerts() {
        viewModelScope.launch {
            val userId = authService.getCurrentFirebaseUserId()
            try {
                val result = userId?.let { reservationService.getTouristAlerts(it) }
                touristAlerts = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                touristAlerts = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun getGuideOffersForTourist() {
        viewModelScope.launch {
            val userId = authService.getCurrentFirebaseUserId()
            try {
                val result = userId?.let { reservationService.getGuideOffersForTourist(it) }
                guideOffersForTourist = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                guideOffersForTourist = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun insertReservationRequest(navHostController: NavHostController) {
        viewModelScope.launch {
            try {
                newReservationRequestStatus = ApiResponse(false, true)
                reservationService.insertReservationRequest(currentReservationRequest)
                newReservationRequestStatus = ApiResponse(true, false)
                val instanceId = authService.getInstanceId(currentReservationRequest.guideUserId)
                FirebaseNotificationMessagingService.sendNotification(
                    getApplication<Application>().resources.getString(R.string.tourist_reservation_resquest_title),
                    "${currentReservationRequest.touristFirstName} ${getApplication<Application>().resources.getString(R.string.tourist_reservation_resquest_body)} ${currentReservationRequest.address.city}",
                    instanceId!!)
                navHostController.popBackStack()
            }
            catch (e: Exception) {
                newReservationRequestStatus = ApiResponse(false, false, true, e.localizedMessage)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun insertTouristAlert(navHostController: NavHostController, tags: MutableList<String>) {
        viewModelScope.launch {
            try {
                newTouristAlertStatus = ApiResponse(false, true)
                currentTouristAlert.experienceTags = tags
                reservationService.insertTouristAlert(currentTouristAlert)
                newTouristAlertStatus = ApiResponse(true, false)
                navHostController.popBackStack()
            }
            catch (e: Exception) {
                newTouristAlertStatus = ApiResponse(false, false, true, e.localizedMessage)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun acceptGuideOffer(guideOffer: GuidingOffer) {
        viewModelScope.launch {
            try {
                acceptGuideOffer = ApiResponse(false, true)
                reservationService.acceptGuideOffer(guideOffer.id)
                acceptGuideOffer = ApiResponse(true, false)
                getGuideOffersForTourist()
                val instanceId = authService.getInstanceId(guideOffer.guideId)
                FirebaseNotificationMessagingService.sendNotification(
                    getApplication<Application>().resources.getString(R.string.tourist_accept_guideOffer_title),
                    "${guideOffer.touristFirstName} ${getApplication<Application>().resources.getString(R.string.tourist_accept_guideOffer_body)} ${guideOffer.touristDestination}",
                    instanceId!!)
            }
            catch (e: Exception) {
                acceptGuideOffer = ApiResponse(true, false)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    fun rejectGuideOffer(guideOffer: GuidingOffer) {
        viewModelScope.launch {
            try {
                rejectGuideOffer = ApiResponse(false, true)
                reservationService.rejectGuideOffer(guideOffer.id)
                rejectGuideOffer = ApiResponse(true, false)
                getGuideOffersForTourist()
                val instanceId = authService.getInstanceId(guideOffer.guideId)
                FirebaseNotificationMessagingService.sendNotification(
                    getApplication<Application>().resources.getString(R.string.tourist_reject_guideOffer_title),
                    "${guideOffer.touristFirstName} ${getApplication<Application>().resources.getString(R.string.tourist_reject_guideOffer_body)}",
                    instanceId!!)
            }
            catch (e: Exception) {
                rejectGuideOffer = ApiResponse(true, false)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    fun deleteTouristAlert(touristAlertId: String) {
        viewModelScope.launch {
            try {
                deleteTouristAlertStatus = ApiResponse(false, true)
                reservationService.deleteTouristAlert(touristAlertId)
                deleteTouristAlertStatus = ApiResponse(true, false)
                getTouristAlerts()
            }
            catch (e: Exception) {
                deleteTouristAlertStatus = ApiResponse(true, false)
                Log.d(ReservationViewModel::class.simpleName, "ERROR: $e")
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

    fun initCurrentTouristAlert() {
        viewModelScope.launch {
            try {
                val currentUserId = authService.getCurrentFirebaseUserId()
                val currentUser = authService.getUserById(currentUserId!!)

                currentTouristAlert = TouristAlert(
                    touristFirstName = "${currentUser!!.firstName} ${currentUser!!.lastName}",
                    touristPhotoUrl = currentUser.profilePhotoUrl,
                    touristCountry = currentUser.address.country,
                    touristId = currentUserId,
                    touristLanguages = currentUser.languages
                )
                initTourisAlertStatus = ApiResponse(data = true, inProgress = false)
            }
            catch (e: Exception) {
                initTourisAlertStatus = ApiResponse(data = false, inProgress = false, hasError = true, errorMessage = e.localizedMessage)
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

    fun updateFromDateTouristAlert(newDate: Date) {
        currentTouristAlert.fromDate = newDate
    }

    fun updateToDateTouristAlert(newDate: Date) {
        currentTouristAlert.toDate = newDate
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