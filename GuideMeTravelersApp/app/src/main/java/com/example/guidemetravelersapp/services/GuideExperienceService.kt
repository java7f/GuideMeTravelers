package com.example.guidemetravelersapp.services

import android.app.Activity
import android.content.Context
import com.example.guidemetravelersapp.dataModels.GuideExperience
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.helpers.SessionManager
import com.example.guidemetravelersapp.interfaces.IGuideExperienceServiceApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GuideExperienceService(context: Context) {
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = GuideExperienceService::class.simpleName
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IGuideExperienceServiceApi::class.java)
    private val sessionManager: SessionManager = SessionManager(context)

    suspend fun getExperience(experienceId: String): GuideExperience {
        return coroutineScope {
            val getExperienceTask = async { apiService.get("api/GuideExperience/$experienceId").body() }
            getExperienceTask.await()!!
        }
    }
}