package com.example.guidemetravelersapp.helpers

import android.content.Context
import android.content.res.Resources
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.services.AuthenticationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add authentication token to API requests
 */
class BasicAuthInterceptor(context: Context) : Interceptor {
    //The session manager that stores the token
    private val sessionManager = SessionManager(context)
    //The session manager that stores the token
    private val context = context


    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader(context.getString(R.string.authorization_header), "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}