package com.example.guidemetravelersapp.models.authentication

import android.content.Context
import android.net.Uri
import com.example.guidemetravelersapp.BuildConfig
import com.example.guidemetravelersapp.helpers.SingletonHolder
import net.openid.appauth.*

class AuthManager private constructor(context: Context){
    private var mAuthState: AuthState?
    private lateinit var mAuth: Auth
    private var mAuthConfig: AuthorizationServiceConfiguration
    private var mSharedPrefRep: SharedPreferencesRepository? = null
    private var mAuthService: AuthorizationService

    init {
        mSharedPrefRep = SharedPreferencesRepository(context = context)
        setAutData()
        mAuthConfig = AuthorizationServiceConfiguration(
            Uri.parse(mAuth.authorizationEndpointUri),
            Uri.parse(mAuth.tokenEndpointUri),
            null
        )
        mAuthState = mSharedPrefRep!!.getAuthState()
        val appAuthConfigurationBuilder: AppAuthConfiguration.Builder = AppAuthConfiguration.Builder()
        val appAuthConfiguration: AppAuthConfiguration = appAuthConfigurationBuilder.build()
        mAuthService = AuthorizationService(context, appAuthConfiguration)
    }

    //Singleton instance for the AuthManager
    companion object: SingletonHolder<AuthManager, Context>(::AuthManager)

    fun getAuth(): Auth {
        return mAuth
    }

    fun getAuthState(): AuthState? {
        return mAuthState
    }

    fun getAuthService(): AuthorizationService {
        return mAuthService
    }

    fun updateAuthState(tokenResponse: TokenResponse, ex: AuthorizationException?) {
        mAuthState?.update(tokenResponse, ex)
        mAuthState?.let { mSharedPrefRep?.saveAuthState(it) }
    }

    fun setAuthState(authorizationResponse: AuthorizationResponse, ex: AuthorizationException) {
        if(mAuthState == null)
            mAuthState = AuthState(authorizationResponse, ex)
        mAuthState?.let { mSharedPrefRep?.saveAuthState(it) }
    }

    private fun setAutData() {
        mAuth = Auth()
        mAuth.clientId = BuildConfig.CLIENT_ID
        mAuth.authorizationEndpointUri = BuildConfig.AUTHORIZATION_END_POINT_URI
        mAuth.clientSecret = BuildConfig.CLIENT_SECRET
        mAuth.scope = BuildConfig.SCOPE
        mAuth.tokenEndpointUri = BuildConfig.TOKEN_END_POINT_URI
        mAuth.responseType = BuildConfig.RESPONSE_TYPE
    }
}