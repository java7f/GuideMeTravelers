package com.example.guidemetravelersapp.services

import android.content.Context
import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.interfaces.ILocationServiceApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class LocationService(context: Context) {

    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(ILocationServiceApi::class.java)

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

    suspend fun getAllAudioguideForLocation(locationId: String): List<Audioguide> {
        return coroutineScope {
            val audioguidesResultTask = async { apiService.getAudioguides("api/Locations/audioguide/$locationId").body() }
            audioguidesResultTask.await()!!
        }
    }

    suspend fun getSearchLocations(query: String): List<Location> {
        return coroutineScope {
            val locationsResultTask = async { apiService.getAllLocations("api/Locations/location/search/$query").body() }
            locationsResultTask.await()!!
        }
    }
}