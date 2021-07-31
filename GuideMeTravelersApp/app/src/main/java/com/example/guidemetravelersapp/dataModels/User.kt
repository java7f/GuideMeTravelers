package com.example.guidemetravelersapp.dataModels

import java.util.*

/**
 * Data class that represents an user in GuideMe
 */
data class User (
    var id: String = "",
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var sex: String = "",
    var birthdate: Date = Date(),
    var country: String = "",
    var phone: String = "",
    var aboutUser: String = "",
    var profilePhotoUrl: String = "",
    var roles: List<String> = emptyList(),
    var reviews: List<Review> = emptyList()
)