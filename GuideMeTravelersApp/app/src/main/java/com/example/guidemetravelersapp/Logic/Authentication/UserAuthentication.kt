package com.example.guidemetravelersapp.Logic.Authentication

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.guidemetravelersapp.R
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException

class UserAuthentication {

    companion object {
        private val TAG: String = UserAuthentication::class.java.simpleName

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        fun CreatePublicClientApplication(
            context: Context,
            onPublicClientApplication: (ISingleAccountPublicClientApplication) -> Unit,
            onAccountLoaded: (IAccount?) -> Unit
        ) {
            PublicClientApplication.createSingleAccountPublicClientApplication(
                context,
                R.raw.auth_config_single_account,
                object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                    override fun onCreated(application: ISingleAccountPublicClientApplication) {
                        onPublicClientApplication(application)
                        LoadAccount(application, onAccountLoaded)
                    }

                    override fun onError(exception: MsalException) {
                        Log.d(TAG, exception.toString())
                    }
                }
            )
        }

        fun SignIn(
            activity: Activity,
            context: Context,
            mSingleAccountApp: ISingleAccountPublicClientApplication?,
            onSuccessfulAuthentication: (IAccount) -> Unit,
            onGraphApiSuccess: (String) -> Unit
        ) {
            mSingleAccountApp?.signIn(
                activity,
                null,
                arrayOf("user.read"),
                AuthenticationCallbackHandler(
                    context,
                    onSuccessfulAuthentication,
                    onGraphApiSuccess
                )
            ) ?: {}
        }

        fun LoadAccount(
            mSingleAccountApp: ISingleAccountPublicClientApplication,
            onAccountLoaded: (IAccount?) -> Unit
        ) {
            if (mSingleAccountApp == null) {
                return
            }

            mSingleAccountApp.getCurrentAccountAsync(object :
                ISingleAccountPublicClientApplication.CurrentAccountCallback {
                override fun onAccountLoaded(activeAccount: IAccount?) {
                    if (activeAccount != null) {
                        onAccountLoaded(activeAccount)
                    }
                }

                override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                    TODO("Not yet implemented")
                }

                override fun onError(exception: MsalException) {
                    TODO("Not yet implemented")
                }
            })
        }

        fun AuthenticationCallbackHandler(
            context: Context,
            onSuccessfulAuthentication: (IAccount) -> Unit,
            onGraphApiSuccess: (String) -> Unit
        ): AuthenticationCallback {
            return object : AuthenticationCallback {
                override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                    /* Successfully got a token, use it to call a protected resource - MSGraph */
                    Log.d(TAG, "Successfully authenticated")
                    if (authenticationResult != null) {
                        Log.d(
                            TAG,
                            "ID Token: " + (authenticationResult.account.claims?.get("id_token"))
                        )
                        onSuccessfulAuthentication(authenticationResult.account)
                        /* call graph */
                        CallGraphApi(authenticationResult, context, onGraphApiSuccess)
                    }

                }

                override fun onError(exception: MsalException?) {
                    Log.d(TAG, "Authentication failed")

                    //displayError()
                }

                override fun onCancel() {
                    Log.d(TAG, "User cancelled login.")
                }
            }
        }

        fun CallGraphApi(
            authenticationResult: IAuthenticationResult,
            context: Context,
            onGraphApiSuccess: (String) -> Unit
        ) {
            MSGraphRequestWrapper.callGraphAPIUsingVolley(
                context,
                MSGraphRequestWrapper.MS_GRAPH_ROOT_ENDPOINT + "v1.0/me",
                authenticationResult.accessToken,
                { response ->
                    run {
                        Log.d(TAG, "Response: $response")
                        onGraphApiSuccess(response.toString())
                    }
                }
            ) { error -> Log.d(TAG, "Error: $error") }
        }
    }
}