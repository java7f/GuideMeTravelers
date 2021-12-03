package com.example.guidemetravelersapp.interfaces

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface INotificationServer {
    @POST("/fcm/send")
    suspend fun post(@Body body: RequestBody, @Header("Authorization") authHeader: String) : Response<Unit>
}