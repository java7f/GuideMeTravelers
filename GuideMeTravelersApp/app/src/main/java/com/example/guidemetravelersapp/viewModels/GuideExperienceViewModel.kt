package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetravelersapp.dataModels.GuideExperience
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.services.GuideExperienceService
import kotlinx.coroutines.launch
import java.lang.Exception

class GuideExperienceViewModel(application: Application) : AndroidViewModel(application) {
    private val guideExperienceService: GuideExperienceService = GuideExperienceService(application)

    var guideExperience: GuideExperience by mutableStateOf(GuideExperience())
    var experienceId: String by mutableStateOf("")

    init {
        getExperience()
    }

    fun updateExperience() {
        getExperience()
    }

    private fun getExperience() {
        viewModelScope.launch {
            try {
                if(!experienceId.isNullOrEmpty()) {
                    val result = guideExperienceService.getExperience(experienceId = experienceId)
                    guideExperience = result
                }
            }
            catch (e: Exception) {
                Log.d(GuideExperienceViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }
}