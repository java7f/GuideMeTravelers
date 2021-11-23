package com.example.guidemetravelersapp.dataModels

data class Review(
    var userId: String = "",
    var userName: String = "",
    var ratingComment: String = "",
    var ratingValue: Float = 0.0f
)