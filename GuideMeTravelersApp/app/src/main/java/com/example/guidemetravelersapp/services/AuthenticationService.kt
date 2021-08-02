package com.example.guidemetravelersapp.services

import android.app.Activity
import android.util.Log
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.helpers.SessionManager
import com.example.guidemetravelersapp.interfaces.IAuthenticationServiceApi
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class AuthenticationService(activity: Activity, private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO ) {
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = AuthenticationService::class.simpleName
    private val retrofitInstance = RetrofitInstance.getRetrofit(activity)
    private val apiService = retrofitInstance.create(IAuthenticationServiceApi::class.java)
    private val sessionManager: SessionManager = SessionManager(activity)

    /**
     * Performs the login action by using the Firebase Authentication instance
     * @param email the user email
     * @param password the user password
     * @return the AuthResult
     */
    suspend fun login(email: String, password: String): AuthResult? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val accessToken = getTokenFromUser()
        if (accessToken != null) {
            sessionManager.saveAuthToken(accessToken)
        }

        return result
    }

    /**
     * Checks if the user has already signed in
     * @return true if the user session exists, false otherwise
     */
    fun isUserLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    /**
     * Signs the usr out.
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Registers a new user in the Firebase user pool and requests the
     * creation of a local user to the GuideMe api.
     * @param email the email of the new user
     * @param password the password for the new user
     * @return true if the registration was successful
     */
    suspend fun registerUser(user: User, password: String): Boolean {
        var result: Boolean = false
        try {
            val registerResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            if (registerResult.user != null) {
                result = createUserInfo(user)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
        }
        return result
    }

    /**
     * Get the access token for the current user
     * @return String with the access token
     */
    suspend fun getTokenFromUser(): String? {
        return auth.currentUser?.getIdToken(true)?.await()?.token
    }


    //region API Methods

    /**
     * Makes the API call to create a new user in the GuideMe application
     * @param user the new User
     * @return true if the http call was successful
     */
    private suspend fun createUserInfo(user: User): Boolean {
        return coroutineScope {
            val createUserTask = async { apiService.post("api/User", user).isSuccessful }
            createUserTask.await()
        }
    }

    //endregion
}