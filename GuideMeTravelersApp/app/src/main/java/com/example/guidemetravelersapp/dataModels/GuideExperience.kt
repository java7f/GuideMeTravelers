package com.example.guidemetravelersapp.dataModels

import java.util.*

data class GuideExperience (
    var id: String = "",
    var guideFirebaseId: String = "",
    var guideFirstName: String = "",
    var guideLastName: String = "",
    var experienceDescription: String = "",
    var guideRating: Float = 0.0f,
    var experiencePrice: Float = 0.0f,
    var guidePhotoUrl: String = "",
    var experienceTags: List<String> = emptyList(),
    var guideReviews: List<Review> = emptyList(),
    var guideAddress: Address = Address()
)