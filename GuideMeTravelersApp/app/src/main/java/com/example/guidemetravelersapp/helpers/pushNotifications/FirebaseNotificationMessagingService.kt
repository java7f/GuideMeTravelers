package com.example.guidemetravelersapp.helpers.pushNotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.helpers.utils.Utils
import com.example.guidemetravelersapp.interfaces.INotificationServer
import com.example.guidemetravelersapp.services.AuthenticationService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit

class FirebaseNotificationMessagingService : FirebaseMessagingService() {

    private var notificationManager: NotificationManager? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] as String
            val body = remoteMessage.data["body"] as String
            displayNotification(applicationContext, title, body)
        } else {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            displayNotification(applicationContext, title!!, body!!)
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    fun createNotificationChannel(id: String, name: String, channelDescription: String, context: Context) {
        notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply { description = channelDescription }

            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun displayNotification(context: Context, title: String, body: String) {
        val notification = NotificationCompat.Builder(context, Utils.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.logo_transparent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        createNotificationChannel(Utils.CHANNEL_ID, Utils.CHANNEL_NAME, Utils.CHANNEL_DESCRIPTION, context = context)
        notificationManager?.notify(Utils.NOTIFICATION_ID, notification)
    }

    companion object {
        fun sendNotification(title: String, body: String, toInstanceId: String) {
            CoroutineScope(Dispatchers.Main).launch {

                val header = "key=AAAAuw2kFw0:APA91bH9EtsXZTDN7dpMGnbAApooowZKthrpq_ye2RC0JdCc6y96Md5mrevfKWfPbDviRhn3O4pJSTpMDPL57rVUbyhUrl7FN30Q1WTOPHshNOYMdtd3X29LhvW8KRm9pPbr-K8F5TYe"
                val retroInstance = Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com")
                    .build()
                val service =  retroInstance.create(INotificationServer::class.java)

                val jsonString = "{\n \"to\" : \"$toInstanceId\",\n \"data\" : {\n     \"body\" : \"$body\",\n     \"title\": \"$title\"\n }\n}"

                val requestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
                val response = service.post(requestBody, header)

                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        Log.i(FirebaseNotificationMessagingService::class.simpleName, "NOTIFICATION SENT")
                    }
                    else {
                        Log.i(FirebaseNotificationMessagingService::class.simpleName, response.message())
                    }
                }
            }
        }
    }
}