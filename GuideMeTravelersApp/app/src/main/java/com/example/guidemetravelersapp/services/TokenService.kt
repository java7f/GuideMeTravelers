package com.example.guidemetravelersapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.guidemetravelersapp.global.GuideMeTravelersApp
import com.example.guidemetravelersapp.models.authentication.AuthManager
import com.example.guidemetravelersapp.models.authentication.SharedPreferencesRepository
import net.openid.appauth.*
import java.util.*

class TokenService : Service() {
    private val notifyAfter: Int = 3000

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        var timer: Timer = Timer("TokenTimer", true)
        timer.scheduleAtFixedRate(TokenTimer(this), 0, notifyAfter.toLong())
    }

    private class TokenTimer(context: Context) : TimerTask() {
        private val mSharedPref: SharedPreferencesRepository = SharedPreferencesRepository(context = context)
        private val context: Context = context
        override fun run() {
            if(GuideMeTravelersApp.token.isNullOrEmpty()) return

            val authManager: AuthManager = AuthManager.getInstance(context)
            val authState: AuthState? = authManager.getAuthState()

            if(authState != null && authState.needsTokenRefresh) {
                var clientSecretPost: ClientSecretPost? = authManager.getAuth().clientSecret?.let {
                    ClientSecretPost(it)
                }

                val request: TokenRequest? = authState.createTokenRefreshRequest()
                val authService: AuthorizationService = authManager.getAuthService()
                authService.performTokenRequest(request!!, clientSecretPost!!, object : AuthorizationService.TokenResponseCallback{
                    override fun onTokenRequestCompleted(
                        response: TokenResponse?,
                        ex: AuthorizationException?
                    ) {
                        if (ex != null) return

                        authManager.updateAuthState(response!!, ex)
                        GuideMeTravelersApp.token = authState.idToken!!
                    }
                })
            }
        }
    }
}