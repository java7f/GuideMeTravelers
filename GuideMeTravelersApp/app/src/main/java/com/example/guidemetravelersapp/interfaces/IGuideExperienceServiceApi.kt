package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.GuideExperience
import retrofit2.Response
import retrofit2.http.*

interface IGuideExperienceServiceApi {
    @GET
    suspend fun get(@Url url:String) : Response<GuideExperience>

    @GET
    suspend fun getAll(@Url url:String) : Response<List<GuideExperience>>

    @POST
    suspend fun post(@Url url:String, @Body body: GuideExperience) : Response<Unit>

    @PUT
    suspend fun put(@Url url:String, @Body body: GuideExperience) : Response<Unit>
}