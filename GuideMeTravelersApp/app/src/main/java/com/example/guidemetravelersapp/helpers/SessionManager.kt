package com.example.guidemetravelersapp.helpers

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.helpers.utils.Utils

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     * @param token the session access token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     * @return String with the current session's access token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveOfflineModeStatus(isOfflineMode: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(Utils.OFFLINE_MODE, isOfflineMode)
        editor.apply()
    }

    fun fetchOfflineMode(): Boolean? {
        return prefs.getBoolean(Utils.OFFLINE_MODE, false)
    }
}