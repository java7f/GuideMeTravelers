package com.example.guidemetravelersapp.services

import android.bluetooth.le.ScanResult
import android.content.Context
import com.accent_systems.ibks_sdk.scanner.ASScannerCallback
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.ASBLeScannerWrapper
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.interfaces.ILocationServiceApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class LocationService(context: Context) {

    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(ILocationServiceApi::class.java)

    //region LOCATIONS
    suspend fun getLocation(id: String): Location {
        return coroutineScope {
            val locationResultTask = async { apiService.getLocation("api/Locations/location/$id").body() }
            locationResultTask.await()!!
        }
    }

    suspend fun getAllLocations(): List<Location> {
        return coroutineScope {
            val locationsResultTask = async { apiService.getAllLocations("api/Locations/location/all").body() }
            locationsResultTask.await()!!
        }
    }

    suspend fun getSearchLocations(query: String): List<Location> {
        return coroutineScope {
            val locationsResultTask = async { apiService.getAllLocations("api/Locations/location/search/$query").body() }
            locationsResultTask.await()!!
        }
    }
    //endregion

    //region AUDIOGUIDES

    suspend fun getAllAudioguideForLocation(locationId: String): List<Audioguide> {
        return coroutineScope {
            val audioguidesResultTask = async { apiService.getAudioguides("api/Locations/audioguide/$locationId").body() }
            audioguidesResultTask.await()!!
        }
    }

    suspend fun getProximityAudioguides(beaconIds: List<String>): List<Audioguide> {
        return coroutineScope {
            val audioguidesResultTask = async { apiService.getProximityAudioguides("api/Locations/audioguide/getProximityAudioguides", beaconIds).body() }
            audioguidesResultTask.await()!!
        }
    }

    //endregion
}