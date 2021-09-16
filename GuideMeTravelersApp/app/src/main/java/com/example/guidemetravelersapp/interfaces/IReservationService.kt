package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IReservationService {
    @GET
    suspend fun getPastExperiencesForTourist(@Url url:String) : Response<List<ExperienceReservation>>
}