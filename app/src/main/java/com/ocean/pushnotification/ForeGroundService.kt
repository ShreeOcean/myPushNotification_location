package com.ocean.pushnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.Timer


class ForeGroundService : Service() {

    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "ForeGroundLocationService"

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(false)
            .setSmallIcon(R.drawable.icons8_bell)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationManager : NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW)

            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(soundUri, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, notificationBuilder.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return super.onStartCommand(intent, flags, startId)
        val timer = Timer()
        LocationHelper().startListeningUserLocation(this, object : MyLocationListener{
            override fun onLocationChanged(location: Location?) {
                mLocation = location
                mLocation?.let {
                    AppExecutors.instance?.networkIO()?.execute {

                    }
                }
            }
        })
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //static object
    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }
}