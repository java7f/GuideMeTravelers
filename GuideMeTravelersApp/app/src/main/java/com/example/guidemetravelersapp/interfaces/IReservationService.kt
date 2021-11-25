package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.*
import retrofit2.Response
import retrofit2.http.*

interface IReservationService {
    @GET
    suspend fun getPastExperiencesForTourist(@Url url:String) : Response<List<ExperienceReservation>>

    @GET
    suspend fun getUpcomingExperiencesForTourist(@Url url:String) : Response<List<ExperienceReservation>>

    @GET
    suspend fun getRequestReservationsForTourist(@Url url:String) : Response<List<ExperienceReservationRequest>>

    @POST
    suspend fun insertReservationRequest(@Url url:String, @Body body: ExperienceReservationRequest) : Response<Unit>

    @PUT
    suspend fun rateExperience(@Url url:String, @Body body: ExperienceReservation) : Response<Unit>

    @GET
    suspend fun getTouristAlerts(@Url url:String) : Response<List<TouristAlert>>

    @GET
    suspend fun getGuideOffersForTourist(@Url url:String) : Response<List<GuidingOffer>>

    @POST
    suspend fun insertTouristAlert(@Url url:String, @Body body: TouristAlert) : Response<Unit>

    @GET
    suspend fun acceptGuideOffer(@Url url: String) : Response<Unit>

    @GET
    suspend fun rejectGuideOffer(@Url url: String) : Response<Unit>

    @DELETE
    suspend fun deleteTouristAlert(@Url url: String) : Response<Unit>
}