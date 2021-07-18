package com.example.guidemetravelersapp.global

import android.app.Application
import com.example.guidemetravelersapp.models.authentication.SharedPreferencesRepository
import net.openid.appauth.AuthState

class GuideMeTravelersApp : Application() {
    companion object {
        lateinit var token: String
    }
    private lateinit var mSharedPref: SharedPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        mSharedPref = SharedPreferencesRepository(this)
        val authState: AuthState? = mSharedPref.getAuthState()
        if (authState != null) {
            token = authState.idToken!!
        }
    }
}