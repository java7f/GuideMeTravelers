package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.Audioguide
import com.example.guidemetravelersapp.dataModels.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ILocationServiceApi {

     @GET
     suspend fun getLocation(@Url url:String): Response<Location>

     @GET
     suspend fun getAllLocations(@Url url:String): Response<List<Location>>

     @GET
     suspend fun getAudioguides(@Url url:String): Response<List<Audioguide>>
}