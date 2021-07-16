package com.example.guidemetravelersapp.Logic.Authentication

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject
import java.lang.Exception


class MSGraphRequestWrapper {

    companion object {
        val TAG: String = MSGraphRequestWrapper::class.java.simpleName
        val MS_GRAPH_ROOT_ENDPOINT: String = "https://graph.microsoft.com/"
        fun callGraphAPIUsingVolley(
            context: Context,
            graphResourceUrl: String,
            accessToken: String,
            responseListener: Response.Listener<JSONObject>,
            errorListener: Response.ErrorListener
        ) {
            if (accessToken == null || accessToken.isEmpty()) {
                return
            }

            var queue: RequestQueue = Volley.newRequestQueue(context)
            var parameters: JSONObject = JSONObject()

            try {
                parameters.put("key", "value")
            } catch (e: Exception) {
                Log.d(TAG, "Failed to put parameters: $e")
            }

            var request: JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                graphResourceUrl,
                parameters,
                responseListener,
                errorListener
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers: HashMap<String, String> = HashMap<String, String>()
                    headers.put("Authorization", "Bearer $accessToken")
                    return headers
                }
            }

            request.setRetryPolicy(
                DefaultRetryPolicy(
                    3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )

            queue.add(request)
        }
    }
}