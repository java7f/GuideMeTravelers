package com.example.guidemetravelersapp.dataModels

data class Review(
    val userId: String = "",
    val userName: String = "",
    val ratingComment: String = "",
    val ratingValue: Float = 0.0f
)