package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.*
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.services.GuideExperieceViewDataService
import com.example.guidemetravelersapp.services.GuideExperienceService
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class HomescreenViewModel(application: Application) : AndroidViewModel(application) {
    private val guideExperienceViewDataService: GuideExperieceViewDataService = GuideExperieceViewDataService(application)

    var guideExperienceViewData: ApiResponse<List<GuideExperienceViewData>> by mutableStateOf(ApiResponse(data = listOf(), inProgress = true))

    var locationSearchValue: String by mutableStateOf("")
    var placesClient: PlacesClient
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()

    init {
        Places.initialize(application, "AIzaSyAn7Hyeg5O-JKSoKUXRmG_I-KMThIDBcDI")
        placesClient = Places.createClient(application)
        fetchExperiencesViewData()
    }

    private fun fetchExperiencesViewData() {
        viewModelScope.launch {
            try {
                val result = guideExperienceViewDataService.getExperiences()
                guideExperienceViewData = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                guideExperienceViewData = ApiResponse(inProgress = false, hasError = true, errorMessage = e.toString())
                Log.d(HomescreenViewModel::class.simpleName, "ERROR: ${e.toString()}")
            }
        }
    }

    fun searchExperiencesViewData(inputText: String) {
        viewModelScope.launch {
            try {
                guideExperienceViewData = ApiResponse(inProgress = true)
                val result = guideExperienceViewDataService.searchExperiences(inputText)
                guideExperienceViewData = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                guideExperienceViewData = ApiResponse(inProgress = false, hasError = true, errorMessage = e.toString())
                Log.d(HomescreenViewModel::class.simpleName, "ERROR: ${e.toString()}")
            }
        }
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
        searchExperiencesViewData(locationSearchValue)
    }
}