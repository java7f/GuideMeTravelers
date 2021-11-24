package com.example.guidemetravelersapp.viewModels

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.helpers.RoutineManager
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.helpers.utils.Utils
import com.example.guidemetravelersapp.services.GuideExperieceViewDataService
import com.example.guidemetravelersapp.services.GuideExperienceService
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class HomescreenViewModel(application: Application) : AndroidViewModel(application) {
    private val guideExperienceViewDataService: GuideExperieceViewDataService = GuideExperieceViewDataService(application)
    private var notificationManager: NotificationManager? = null
    private var fusedLocationClient: FusedLocationProviderClient

    var guideExperienceViewData: ApiResponse<List<GuideExperienceViewData>> by mutableStateOf(ApiResponse(data = listOf(), inProgress = true))

    var locationSearchValue: String by mutableStateOf("")
    var currentCityLocation: String by mutableStateOf("")
    var placesClient: PlacesClient
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()

    init {
        placesClient = Places.createClient(application)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        fetchExperiencesViewData()
        notificationManager = getSystemService(application, NotificationManager::class.java) as NotificationManager
        createNotificationChannel(Utils.CHANNEL_ID, Utils.CHANNEL_NAME, Utils.CHANNEL_DESCRIPTION)
        registerScanRoutine()
    }

    fun fetchExperiencesViewData() {
        viewModelScope.launch {
            try {
                if (ActivityCompat.checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@launch
                }
                else {
                    val currentLocation = fusedLocationClient.lastLocation.await()
                    val geocoder = Geocoder(getApplication(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                    if (addresses.size > 0) {
                        currentCityLocation = addresses[0].locality
                        val result = guideExperienceViewDataService.searchExperiences(currentCityLocation)
                        guideExperienceViewData = ApiResponse(data = result, inProgress = false)
                    }
                }
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
        currentCityLocation = locationSearchValue
        searchExperiencesViewData(locationSearchValue)
    }

    fun registerScanRoutine() {
        val scannerWrapper = ASBLeScannerWrapper.getInstance()

        RoutineManager.post(object : Runnable {
            override fun run() {
                val scanBeaconsTask = CoroutineScope(Dispatchers.IO).launch {
                    ASBLeScannerWrapper.scannedDevicesList = mutableMapOf()
                    scannerWrapper.startScan()
                }
                viewModelScope.launch {
                    scanBeaconsTask.join()
                    if(ASBLeScannerWrapper.scannedDevicesList.isNotEmpty())
                        displayNotification()
                }
                RoutineManager.mainHandler.postDelayed(this, 30000)
            }
        })
    }

    fun createNotificationChannel(id: String, name: String, channelDescription: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply { description = channelDescription }

            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun displayNotification() {

        val title = getApplication<Application>().resources.getString(R.string.proximity_recomendation_title)
        val content = getApplication<Application>().resources.getString(R.string.proximity_recomendation_content)
        val notification = NotificationCompat.Builder(getApplication(), Utils.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("${ASBLeScannerWrapper.scannedDevicesList.size} $content")
            .setSmallIcon(R.drawable.logo_transparent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager?.notify(Utils.NOTIFICATION_ID, notification)
    }
}