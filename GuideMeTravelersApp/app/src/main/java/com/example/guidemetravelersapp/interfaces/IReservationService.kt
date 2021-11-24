package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.ExperienceReservation
import com.example.guidemetravelersapp.dataModels.ExperienceReservationRequest
import com.example.guidemetravelersapp.dataModels.GuideExperience
import com.example.guidemetravelersapp.dataModels.TouristAlert
import retrofit2.Response
import retrofit2.http.*

interface IReservationService {
    @GET
    suspend fun getPastExperiencesForTourist(@Url url:String) : Response<List<ExperienceReservation>>

    @POST
    suspend fun insertReservationRequest(@Url url:String, @Body body: ExperienceReservationRequest) : Response<Unit>

    @PUT
    suspend fun rateExperience(@Url url:String, @Body body: ExperienceReservation) : Response<Unit>

    @GET
    suspend fun getTouristAlerts(@Url url:String) : Response<List<TouristAlert>>

    @POST
    suspend fun insertTouristAlert(@Url url:String, @Body body: TouristAlert) : Response<Unit>
}