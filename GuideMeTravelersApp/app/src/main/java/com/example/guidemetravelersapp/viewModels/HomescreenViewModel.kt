package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.guidemetravelersapp.dataModels.viewData.GuideExperienceViewData
import com.example.guidemetravelersapp.services.GuideExperieceViewDataService
import com.example.guidemetravelersapp.services.GuideExperienceService
import kotlinx.coroutines.launch
import java.lang.Exception

class HomescreenViewModel(application: Application) : AndroidViewModel(application) {
    private val guideExperienceViewDataService: GuideExperieceViewDataService = GuideExperieceViewDataService(application)

    var guideExperienceViewData: List<GuideExperienceViewData> by mutableStateOf(listOf())

    init {
        fetchExperiencesViewData()
    }

    private fun fetchExperiencesViewData() {
        viewModelScope.launch {
            try {
                val result = guideExperienceViewDataService.getExperiences()
                guideExperienceViewData = result
            }
            catch (e: Exception) {
                Log.d(HomescreenViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }
}