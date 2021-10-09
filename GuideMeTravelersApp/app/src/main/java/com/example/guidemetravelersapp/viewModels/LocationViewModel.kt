package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.services.LocationService
import kotlinx.coroutines.launch
import java.lang.Exception

class LocationViewModel(application: Application): AndroidViewModel(application) {

    private val locationService: LocationService = LocationService(application)

    var locations: ApiResponse<List<Location>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var audioguides: ApiResponse<List<Audioguide>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var currentLocation: ApiResponse<Location> by mutableStateOf(ApiResponse(data = Location(), inProgress = true))
    var currentAudioguideUrl: String by mutableStateOf("")

    init {
        getLocations()
    }

    fun getLocations() {
        viewModelScope.launch {
            try {
                val result = locationService.getAllLocations()
                locations = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                locations = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
            }
        }
    }

    fun getAudioguidesForLocation(locationId: String) {
        viewModelScope.launch {
            try {
                val result = locationService.getAllAudioguideForLocation(locationId)
                audioguides = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                audioguides = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
            }
        }
    }

    fun getLocation(locationId: String) {
        viewModelScope.launch {
            try {
                val result = locationService.getLocation(locationId)
                currentLocation = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                currentLocation = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
            }
        }
    }
}