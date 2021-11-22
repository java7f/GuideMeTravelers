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
import com.downloader.*
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.helpers.RoutineManager
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class LocationViewModel(application: Application): AndroidViewModel(application), OnDownloadListener {

    private val locationService: LocationService = LocationService(application)
    private val context = application
    private var offlineDatabase = GuideMeOffline.getDatabase(application)
    private val sessionManager: SessionManager = SessionManager(application)
    private var audioguidesToDownload: MutableList<Audioguide> = mutableListOf()
    private var currentDecryptedAudio: ByteArray by mutableStateOf(byteArrayOf())

    var locations: ApiResponse<List<Location>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var audioguides: ApiResponse<List<Audioguide>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var proximityRecommendedAudioguides: ApiResponse<List<Audioguide>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var currentLocation: ApiResponse<Location> by mutableStateOf(ApiResponse(data = Location(), inProgress = true))
    var currentAudioguideUrl: String by mutableStateOf("")

    var locationSearchValue: String by mutableStateOf("")
    var placesClient: PlacesClient
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()

    var currentEncryptingAudio: Audioguide by mutableStateOf(Audioguide())
    var currentAudioguideDownloadProgress: Float by mutableStateOf(0.0f)
    var isLoadingDecrypting : Boolean by mutableStateOf(false)

    init {
        Places.initialize(application, "AIzaSyAn7Hyeg5O-JKSoKUXRmG_I-KMThIDBcDI")
        placesClient = Places.createClient(application)
        getLocations()
        RoutineManager.cancelRoutines()
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

    fun getProximityAudioguides() {
        if(!sessionManager.fetchOfflineMode()!!)
            getProximityAudioguidesOnline()
        else
            getProximityAudioguidesOffline()
    }

    fun getProximityAudioguidesOnline() {
        viewModelScope.launch {
            if(!ASBLeScannerWrapper.scannedDevicesList.isNullOrEmpty()) {
                try {
                    var top3ScannedDevices = ASBLeScannerWrapper.scannedDevicesList.toSortedMap(
                        compareByDescending { ASBLeScannerWrapper.scannedDevicesList[it] })
                    val result =
                        locationService.getProximityAudioguides(currentLocation.data!!.id, top3ScannedDevices.keys.toList().take(3))
                    proximityRecommendedAudioguides =
                        ApiResponse(data = result, inProgress = false)
                } catch (e: Exception) {
                    Log.d(
                        LocationViewModel::class.simpleName,
                        "ERROR: ${e.localizedMessage}"
                    )
                    proximityRecommendedAudioguides = ApiResponse(
                        inProgress = false,
                        hasError = true,
                        errorMessage = e.localizedMessage
                    )
                }
            }
            else {
                proximityRecommendedAudioguides = ApiResponse(data = mutableListOf(), inProgress = false)
            }
        }
    }

    fun getProximityAudioguidesOffline() {
        viewModelScope.launch {
            if(!ASBLeScannerWrapper.scannedDevicesList.isNullOrEmpty()) {
                try {
                    var top3ScannedDevices = ASBLeScannerWrapper.scannedDevicesList.toSortedMap(
                        compareByDescending { ASBLeScannerWrapper.scannedDevicesList[it] })
                    val result = offlineDatabase.audioguideDao().getProximityAudioguide(currentLocation.data!!.id, top3ScannedDevices.keys.toList().take(3))
                    proximityRecommendedAudioguides = ApiResponse(data = result, inProgress = false)
                } catch (e: Exception) {
                    Log.d(LocationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                    proximityRecommendedAudioguides = ApiResponse(
                        inProgress = false,
                        hasError = true,
                        errorMessage = e.message!!
                    )
                }
            }
            else {
                proximityRecommendedAudioguides = ApiResponse(data = mutableListOf(), inProgress = false)
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

    fun saveLocation() {
        val fileName = currentLocation.data!!.locationPhotoUrl.split('/').last()
        PRDownloader.download(
            currentLocation.data!!.locationPhotoUrl,
            FileUtils.getLocationImagesPath(context),
            fileName
        ).build()
            .start(object : OnDownloadListener{
                override fun onDownloadComplete() {
                    currentLocation.data!!.locationOfflinePath = "${FileUtils.getLocationImagesPath(context)}${File.separator}$fileName"
                    saveLocationToOfflineDb()
                }
                override fun onError(error: Error?) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun saveLocationToOfflineDb() {
        viewModelScope.launch {
            offlineDatabase.locatioDao().insertLocation(currentLocation.data!!)
        }
    }

    fun saveAudioguide() {
        viewModelScope.launch {
            currentEncryptingAudio.isDownloaded = true
            offlineDatabase.audioguideDao().insertAudioguide(currentEncryptingAudio)
        }
    }

    fun isAudioguideDownloaded(audioId: String) {
        viewModelScope.launch {
            val audioguide = offlineDatabase.audioguideDao().getAudioguide(audioId)
            if(audioguide != null)
                audioguides.data!!.find { it.id == audioguide.id }?.isDownloaded = true
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
        saveLocation()
        if (audioguides.data?.isNotEmpty() == true) {
            audioguidesToDownload = (audioguides.data as MutableList<Audioguide>?)!!
            currentEncryptingAudio = audioguidesToDownload.removeFirst()
            PRDownloader.download(
                currentEncryptingAudio.audioguideUrl,
                FileUtils.getDirPath(context),
                currentEncryptingAudio.audiofileName
            )
            .build()
            .setOnProgressListener { progress ->
                if (progress != null) {
                    currentAudioguideDownloadProgress = progress.currentBytes.toFloat() / progress.totalBytes.toFloat()
                    Log.i("DOWNLOAD PROGRESS", currentAudioguideDownloadProgress.toString())
                }
            }
                .start(this)
            Log.i(DownloadTestActivity::class.simpleName, "${currentEncryptingAudio.name} File is downloading")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun encryptAudio() {
        Log.i(DownloadTestActivity::class.simpleName, "File is being encrypted")
        try {
            val filePath = FileUtils.buildFilePath(context, currentEncryptingAudio.audiofileName)
            val fileData = FileUtils.readFile(filePath)
            val fileEncoded = EncryptDecryptHelper.encode(fileData)
            FileUtils.saveFile(fileEncoded, filePath)
            currentEncryptingAudio.audioguideUrl = filePath
            Log.i(DownloadTestActivity::class.simpleName, "File encrypted")
        }
        catch (e: Exception) {
            Log.e(DownloadTestActivity::class.simpleName, e.message!!)
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun decryptAudio(audioguideName: String) {
        try {
            isLoadingDecrypting = true
            val fileData = FileUtils.readFile(FileUtils.buildFilePath(context, audioguideName))
            val fileDecoded = EncryptDecryptHelper.decode(fileData)
            Log.i(DownloadTestActivity::class.simpleName, "File decoded!!")
            currentDecryptedAudio = fileDecoded
            isLoadingDecrypting = false
        }
        catch (e: Exception) {
            Log.e(DownloadTestActivity::class.simpleName, e.message!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun playAudio(audioParam: String) {
        if(!sessionManager.fetchOfflineMode()!!)
            playAudioOnline(audioParam)
        else
            playAudioOffline(audioParam)
    }

    private fun playAudioOnline(remoteUrl: String) {
        currentAudioguideUrl = remoteUrl
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun playAudioOffline(audioName: String) {
        val decryptTask = CoroutineScope(Dispatchers.IO).launch {
            decryptAudio(audioName)
            val tempFile = FileUtils.createTempFile(getApplication(), currentDecryptedAudio)
            currentAudioguideUrl = tempFile.absolutePath
        }
        viewModelScope.launch {
            decryptTask.join()
        }
    }

    fun getOfflineModeStatus(): Boolean {
        return sessionManager.fetchOfflineMode()!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDownloadComplete() {
        Log.i(DownloadTestActivity::class.simpleName, "File downloaded!!")
        val encryptTask = CoroutineScope(Dispatchers.IO).launch {
            encryptAudio()
        }
        viewModelScope.launch {
            encryptTask.join()
            saveAudioguide()
            currentAudioguideDownloadProgress = 0.0f
            if(audioguidesToDownload.isNotEmpty()) {
                downloadFile()
            }
            else {
                currentEncryptingAudio = Audioguide()
            }
        }
    }

    override fun onError(error: Error?) {
        Log.e(DownloadTestActivity::class.simpleName, error.toString())
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
                    getProximityAudioguides()
                }
                RoutineManager.mainHandler.postDelayed(this, 15000)
            }
        })
    }
}