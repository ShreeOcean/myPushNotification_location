package com.ocean.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseMessageReceiver : FirebaseMessagingService() {

    val TAG = FirebaseMessageReceiver::class.java.simpleName
    val channelId = "com.ocean.pushnotification.NotifyID"
    lateinit var intent : Intent
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refresh token : $token")
        /** If you want to send messages to this application instance or
         * manage this apps subscriptions on the server side, send the
         * Instance ID token to your app server. */
        //send registration to server.

        super.onNewToken(token)
    }



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //handle FCM msg here
        Log.d(TAG, "From ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (!remoteMessage.data.isNullOrEmpty()){
                val msg : String = remoteMessage.data["message"].toString()
                val title : String = remoteMessage.notification!!.title.toString()
                showNotification(title,msg)
            }
        }
        remoteMessage.notification?.let {
            showNotification(
                remoteMessage.notification!!.title.toString(),
                remoteMessage.notification!!.body.toString(),
                //image url
            )
            Log.d(TAG, "Notification Message Body: ${it.body}")
        }


    }

    private fun showNotification(title: String, msg: String) {

        val targetClass = if (title == "Demo Notification 2"){
            MainActivity2::class.java
        }else{
            MainActivity ::class.java
        }

        intent = Intent(this, targetClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val channelId = getString(R.string.default_notification_channel_id)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.icons8_bell)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setVibrate(
                    longArrayOf(
                        1000, 1000, 1000,
                        1000, 1000
                    )
                )
                .setOnlyAlertOnce(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .build()


        //since oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId, "ocean_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        val notificationID = Random.nextInt()
        notificationManager.notify(notificationID, notification)
    }

    fun sendRegistrationToServer(token: String){
        Log.e("FCM TOKEN", token!!);
    }
}