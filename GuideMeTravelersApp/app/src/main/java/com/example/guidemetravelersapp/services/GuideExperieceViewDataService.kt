package com.example.guidemetravelersapp.services

import android.app.Activity
import android.content.Context
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.helpers.SessionManager
import com.example.guidemetravelersapp.interfaces.IGuideExperienceServiceApi
import com.example.guidemetravelersapp.interfaces.IGuideExperienceViewDataServiceApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GuideExperieceViewDataService(context: Context) {
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = GuideExperienceService::class.simpleName
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IGuideExperienceViewDataServiceApi::class.java)
    private val sessionManager: SessionManager = SessionManager(context)

    suspend fun getExperiences(): List<GuideExperienceViewData> {
        return coroutineScope {
            val getExperiencesViewDataTask = async { apiService.getAll("api/GuideExperienceViewData").body() }
            getExperiencesViewDataTask.await()!!
        }
    }
}