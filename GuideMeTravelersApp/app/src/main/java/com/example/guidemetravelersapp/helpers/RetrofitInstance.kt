package com.example.guidemetravelersapp.helpers

import android.content.Context
import androidx.compose.ui.res.integerArrayResource
import com.example.guidemetravelersapp.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class will return the Retrofit
 * instance that will be used for API calls
 */
class RetrofitInstance() {
    companion object {
        /**
         * Retrieves the retrofit instance for making HTTP requests
         */
        fun getRetrofit(context: Context): Retrofit {
            //Specify the date format to be serializable and deserializable
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL) //API Url
                .addConverterFactory(GsonConverterFactory.create(gson)) //Used to convert Json to a local Data Model
                .client(okHttpClient(context))
                .build()
        }

        /**
         * Builds the Http client
         */
        private fun okHttpClient(context: Context): OkHttpClient {
            var interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .authenticator(TokenAuthenticator(context))
                .build()
        }
    }
}