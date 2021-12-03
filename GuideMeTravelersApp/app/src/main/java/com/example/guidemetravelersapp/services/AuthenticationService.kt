package com.example.guidemetravelersapp.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.helpers.SessionManager
import com.example.guidemetravelersapp.interfaces.IAuthenticationServiceApi
import com.example.guidemetravelersapp.views.homescreen.HomeScreen
import com.example.guidemetravelersapp.views.loginView.LoginActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.InputStream
import java.net.URI

class AuthenticationService(context: Context) {
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = AuthenticationService::class.simpleName
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IAuthenticationServiceApi::class.java)
    private val sessionManager: SessionManager = SessionManager(context)
    private val context: Context = context

    /**
     * Performs the login action by using the Firebase Authentication instance
     * @param email the user email
     * @param password the user password
     * @return the AuthResult
     */
    suspend fun login(email: String, password: String): AuthResult? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val accessToken = getTokenFromUser()
        Log.d(TAG, accessToken!!)
        if (accessToken != null) {
            sessionManager.saveAuthToken(accessToken)
            val instanceId = FirebaseMessaging.getInstance().token.await()
            saveInstanceId(instanceId = instanceId, userId = auth.currentUser!!.uid)
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
        context.startActivity(Intent(context, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
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
            if (!validateNewUser(user.email))
                return false
            val registerResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            if (registerResult.user != null) {
                user.firebaseUserId = registerResult.user?.uid!!
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

    private suspend fun validateNewUser(email: String): Boolean {
        val isNewUser = auth.fetchSignInMethodsForEmail(email).await()
        return isNewUser.signInMethods?.isEmpty()!!
    }

    /**
     * Gets the current logged in firebase user
     * @return the current FirebaseUser
     */
    fun getCurrentFirebaseUser(): FirebaseUser? {
        return if (isUserLoggedIn())
            auth.currentUser!!
        else
            null
    }

    /**
     * Gets the current logged in firebase user email
     * @return the current email
     */
    fun getCurrentFirebaseUserEmail(): String? {
        return if (isUserLoggedIn())
            auth.currentUser!!.email
        else
            null
    }

    /**
     * Gets the current logged in firebase user id
     * @return the current id
     */
    fun getCurrentFirebaseUserId(): String? {
        return if (isUserLoggedIn())
            auth.currentUser!!.uid
        else
            null
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

    /**
     * Makes the API call to a user given its email
     * @param email the user email
     * @return the requested User, null if not found
     */
    @Deprecated(
        message = "Use the Firebase uid to get the user info instead",
        replaceWith = ReplaceWith("getUserById")
    )
    suspend fun getUserByEmail(email: String): User? {
        return coroutineScope {
            val user = async { apiService.getByEmail("api/User/getByEmail/$email").body() }
            user.await()
        }
    }

    suspend fun getUserById(id: String): User? {
        return coroutineScope {
            val user = async { apiService.getByEmail("api/User/$id").body() }
            user.await()
        }
    }

    suspend fun getInstanceId(userId: String): String? {
        return coroutineScope {
            val user = async { apiService.getInstanceId("api/User/getGuideInstanceId/$userId").body() }
            user.await()
        }
    }

    suspend fun saveInstanceId(instanceId: String, userId: String) {
        return coroutineScope {
            val user = async { apiService.saveInstanceId("api/User/saveTouristInstanceId/$instanceId/$userId").body() }
            user.await()
        }
    }

    suspend fun updateUser(user: User): Boolean {
        return coroutineScope {
            val updateUserTask = async { apiService.update("api/User", user).isSuccessful }
            updateUserTask.await()
        }
    }

    suspend fun updateUserProfilePhoto(user: User, uri: Uri): Boolean {
        return coroutineScope {
            var name = DocumentFile.fromSingleUri(context, uri)?.name
            val prefixAndSuffix = name!!.split(".")
            val file = File.createTempFile(prefixAndSuffix[0], ".${prefixAndSuffix[0]}")
            file.deleteOnExit()
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)!!
            FileUtils.copyInputStreamToFile(inputStream, file)
            val requestBody: RequestBody = file.asRequestBody(context.contentResolver.getType(uri)!!.toMediaTypeOrNull())
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("profile_photo", name, requestBody)
            val updateUserPhotoTask = async { apiService.update_photo("api/User/uploadFile/${user.email}", body).isSuccessful }
            updateUserPhotoTask.await()
        }
    }

    //endregion
}
