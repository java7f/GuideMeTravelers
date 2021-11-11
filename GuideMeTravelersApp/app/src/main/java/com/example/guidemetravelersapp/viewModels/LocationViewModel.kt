package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.SessionManager
import com.example.guidemetravelersapp.helpers.encryption.EncryptDecryptHelper
import com.example.guidemetravelersapp.helpers.encryption.FileUtils
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.helpers.roomDatabases.GuideMeOffline
import com.example.guidemetravelersapp.services.LocationService
import com.example.guidemetravelersapp.views.audioGuideLocation.DownloadTestActivity
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import java.lang.Exception

class LocationViewModel(application: Application): AndroidViewModel(application), OnDownloadListener {

    private val locationService: LocationService = LocationService(application)
    private val context = application
    private var currentEncryptingAudio: Audioguide by mutableStateOf(Audioguide())
    private var offlineDatabase = GuideMeOffline.getDatabase(application)
    private val sessionManager: SessionManager = SessionManager(application)

    var locations: ApiResponse<List<Location>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var audioguides: ApiResponse<List<Audioguide>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var currentLocation: ApiResponse<Location> by mutableStateOf(ApiResponse(data = Location(), inProgress = true))
    var currentAudioguideUrl: String by mutableStateOf("")

    var locationSearchValue: String by mutableStateOf("")
    var placesClient: PlacesClient
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()

    init {
        Places.initialize(application, "AIzaSyAn7Hyeg5O-JKSoKUXRmG_I-KMThIDBcDI")
        placesClient = Places.createClient(application)
        getLocations()
    }

    fun getLocations() {
        if(!sessionManager.fetchOfflineMode()!!)
            getLocationsOnline()
        else
            getLocationsOffline()
    }

    fun getLocationsOnline() {
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

    fun getLocationsOffline() {
        viewModelScope.launch {
            try {
                val result = offlineDatabase.locatioDao().getLocations()
                locations = ApiResponse(result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                locations = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
            }
        }
    }

    fun getAudioguidesForLocation(locationId: String) {
        if(!sessionManager.fetchOfflineMode()!!)
            getAudioguidesForLocationOnline(locationId)
        else
            getAudioguidesForLocationOffline(locationId)
    }

    fun getAudioguidesForLocationOnline(locationId: String) {
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

    fun getAudioguidesForLocationOffline(locationId: String) {
        viewModelScope.launch {
            try {
                val result = offlineDatabase.audioguideDao().getAudioguidesForLocation(locationId)
                audioguides = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                audioguides = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
            }
        }
    }

    fun getLocation(locationId: String) {
        if(!sessionManager.fetchOfflineMode()!!)
            getLocationOnline(locationId)
        else
            getLocationOffline(locationId)
    }

    fun getLocationOnline(locationId: String) {
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

    fun getLocationOffline(locationId: String) {
        viewModelScope.launch {
            try {
                val result = offlineDatabase.locatioDao().getLocation(locationId)
                currentLocation = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                currentLocation = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
            }
        }
    }

    fun saveLocation(locationId: String) {
        viewModelScope.launch {
            getLocation(locationId)
            offlineDatabase.locatioDao().insertLocation(currentLocation.data!!)
        }
    }

    fun saveAudioguide() {
        viewModelScope.launch {
            offlineDatabase.audioguideDao().insertAudioguide(currentEncryptingAudio)
        }
    }

    fun searchLocationsByPlace(query: String) {
        viewModelScope.launch {
            try {
                locations = ApiResponse(inProgress = true)
                val result = locationService.getSearchLocations(query)
                locations = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                locations = ApiResponse(inProgress = false, hasError = true, errorMessage = e.localizedMessage )
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
        searchLocationsByPlace(locationSearchValue)
    }

    fun downloadFile() {
        if (audioguides.data?.isNotEmpty() == true) {
            for (audioguide in audioguides.data!!) {
                currentEncryptingAudio = audioguide
                PRDownloader.download(
                    audioguide.audioguideUrl,
                    FileUtils.getDirPath(context),
                    audioguide.audiofileName
                ).build().start(this)
                Log.i(DownloadTestActivity::class.simpleName, "${audioguide.name} File is downloading")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun encryptAudio() {
        Log.i(DownloadTestActivity::class.simpleName, "File is being encrypted")
        try {
            val filePath = FileUtils.buildFilePath(context, currentEncryptingAudio.audiofileName)
            val fileData = FileUtils.readFile(filePath)
            val fileEncoded = EncryptDecryptHelper.encode(fileData)
            FileUtils.saveFile(fileEncoded, filePath)
            Log.i(DownloadTestActivity::class.simpleName, "File encrypted")
        }
        catch (e: Exception) {
            Log.e(DownloadTestActivity::class.simpleName, e.message!!)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun decryptAudio(): ByteArray? {
        try {
            val fileData = FileUtils.readFile(FileUtils.buildFilePath(context, "test.mp3"))
            val fileDecoded = EncryptDecryptHelper.decode(fileData)
            Log.i(DownloadTestActivity::class.simpleName, "File decoded!!")
            return fileDecoded
        }
        catch (e: Exception) {
            Log.e(DownloadTestActivity::class.simpleName, e.message!!)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDownloadComplete() {
        Log.i(DownloadTestActivity::class.simpleName, "File downloaded!!")
        encryptAudio()
        //saveAudioguide()
    }

    override fun onError(error: Error?) {
        Log.e(DownloadTestActivity::class.simpleName, error.toString())
    }


}