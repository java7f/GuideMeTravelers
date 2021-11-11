package com.example.guidemetravelersapp.dataModels.viewData

import com.example.guidemetravelersapp.dataModels.Address

data class GuideExperienceViewData (
    var id: String = "",
    var guideExperienceId: String = "",
    var guideFirstName: String = "",
    var guideLastName: String = "",
    var guideRating: Float = 0.0f,
    var experiencePrice: Float = 0.0f,
    var guidePhotoUrl: String = "",
    var experienceTags: List<String> = emptyList(),
    var guideAddress: Address = Address()
)