package com.example.guidemetravelersapp.helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.guidemetravelersapp.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.Route
import java.net.Proxy

class TokenAuthenticator(context: Context) : okhttp3.Authenticator {
    //The session manager that stores the token
    private val sessionManager = SessionManager(context)
    //The session manager that stores the token
    private val context = context
    private var auth: FirebaseAuth = Firebase.auth

    override fun authenticate(route: Route?, response: okhttp3.Response): okhttp3.Request {
        val task: Task<GetTokenResult> = auth.currentUser?.getIdToken(true) as Task<GetTokenResult>
        var newAccessToken = Tasks.await(task).token

        return response.request.newBuilder()
            .header(context.getString(R.string.authorization_header), "Bearer $newAccessToken")
            .build()
    }
}