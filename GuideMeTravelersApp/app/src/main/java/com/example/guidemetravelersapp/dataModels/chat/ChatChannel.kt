package com.example.guidemetravelersapp.dataModels.chat

data class ChatChannel(
    var id: String = "",
    var sentBy_Id: String = "", //User who sends a message
    var sentTo_Id: String = "", //User who receives a massage
)
