package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API interface for the Authentication Service
 */
interface IAuthenticationServiceApi {
    @POST
    suspend fun post(@Url url:String, @Body body: User) : Response<Unit>

    @GET
    suspend fun getByEmail(@Url url:String) : Response<User>

    @PUT
    suspend fun update(@Url url:String, @Body body: User) : Response<Unit>

    @Multipart
    @POST
    suspend fun update_photo(@Url url:String,
                       @Part file: MultipartBody.Part) : Response<Unit>
}