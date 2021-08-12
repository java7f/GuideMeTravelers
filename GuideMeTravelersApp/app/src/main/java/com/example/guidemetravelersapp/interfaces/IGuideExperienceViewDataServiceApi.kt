package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IGuideExperienceViewDataServiceApi {
    @GET
    suspend fun getAll(@Url url:String) : Response<List<GuideExperienceViewData>>
}