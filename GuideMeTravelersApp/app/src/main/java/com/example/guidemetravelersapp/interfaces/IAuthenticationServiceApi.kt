package com.example.guidemetravelersapp.interfaces

import com.example.guidemetravelersapp.dataModels.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * API interface for the Authentication Service
 */
interface IAuthenticationServiceApi {
    @POST
    suspend fun post(@Url url:String, @Body body: User) : Response<Unit>

    @GET
    suspend fun getByEmail(@Url url:String) : Response<User>
}