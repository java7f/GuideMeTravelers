package com.example.guidemetravelersapp.models.authentication

import android.content.Context
import androidx.preference.PreferenceManager
import net.openid.appauth.AuthState
import org.json.JSONException

class SharedPreferencesRepository(context: Context) {
    private var mContext: Context? = context

    fun saveCodeVerifier(codeVerifier: String) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("Auth.CodeVerifier",codeVerifier).apply()
    }

    fun getCodeVerifier(): String? {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getString("Auth.CodeVerifier", "")
    }

    fun saveAuthState(authState: AuthState) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("AuthState",authState.jsonSerializeString()).apply()
    }

    fun getAuthState(): AuthState? {
        val authStateString = PreferenceManager.getDefaultSharedPreferences(mContext).getString("AuthState",null)
        if(!authStateString.isNullOrEmpty()) {
            try {
                return AuthState.jsonDeserialize(authStateString)
            } catch (e: JSONException) {
                return null
            }
        }
        return null
    }
}