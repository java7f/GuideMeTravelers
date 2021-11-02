package com.example.guidemetravelersapp.viewModels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.services.AuthenticationService
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileService: AuthenticationService = AuthenticationService(application)

    var profileData: ApiResponse<User> by mutableStateOf(ApiResponse(data = User(), inProgress = true))
    var updateProfileResult: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var editableUser: User by mutableStateOf(User())

    init {
        getCurrentUserProfile()
    }

    private fun getCurrentUserProfile() {
        viewModelScope.launch {
            try {
                var currentUser = profileService.getCurrentFirebaseUser()
                val result = currentUser?.uid?.let { profileService.getUserById(it) }
                profileData = ApiResponse(data = result, inProgress = false)
                editableUser = result!!
            }
            catch (e: Exception) {
                Log.d(HomescreenViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                profileData = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun saveProfileChange(fileUri: Uri?) {
        viewModelScope.launch {
            try {
                updateProfileResult = ApiResponse(false, true)
                val result = profileService.updateUser(editableUser)
                var photo_result = false
                if(fileUri != null) {
                    photo_result = profileService.updateUserProfilePhoto(editableUser, fileUri)
                }
                val updateSuccessful = result && photo_result
                profileData = ApiResponse(data = editableUser, inProgress = false)
                updateProfileResult = ApiResponse(data = updateSuccessful, inProgress = false)
            } catch (e: Exception) {
                Log.d(HomescreenViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                profileData = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    /**
     * Calculates the average user rating given all the reviews for the
     * specified user.
     * @param user The user to make the calculation
     * @return Float with the average rating
     */
    fun calculateUserRating(): Float {
        var totalRating = 0.0f
        if (profileData.data!!.reviews.isEmpty())
            return totalRating
        else {
            profileData.data!!.reviews.forEach { review ->
                totalRating += review.ratingValue
            }
        }
        return totalRating / profileData.data!!.reviews.size
    }
}