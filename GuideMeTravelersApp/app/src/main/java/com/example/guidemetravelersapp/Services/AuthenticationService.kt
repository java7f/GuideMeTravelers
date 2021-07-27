package com.example.guidemetravelersapp.Services

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class AuthenticationService(activity: Activity) {
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = AuthenticationService::class.simpleName
    private var activity: Activity = activity

    /**
     * Performs the login action by using the Firebase Authentication instance
     * @param email the user email
     * @param password the user password
     * @return true if the authentication was successful
     */
    suspend fun login(email: String, password: String): AuthResult? {
        return auth.signInWithEmailAndPassword(email, password).await()
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
    fun registerUser(email: String, password: String): Boolean {
        var result: Boolean = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    result = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        return result
    }

    suspend fun getTokenFromUser(): String? {
        return auth.currentUser?.getIdToken(true)?.await()?.token
    }
}