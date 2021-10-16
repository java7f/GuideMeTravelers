package com.example.guidemetravelersapp.dataModels.chat

import java.util.*

data class Message(
    var message: String = "",
    var sent_by: String = "",
    var sent_to: String = "",
    var sent_on: Date = Date(),
    var currentUser: Boolean = false,
)
