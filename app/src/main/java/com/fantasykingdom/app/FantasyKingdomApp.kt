package com.fantasykingdom.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point.
 *
 * Annotated with [HiltAndroidApp] so Hilt can generate the DI container that
 * every Activity / ViewModel in the app hangs off of. Also responsible for
 * one-time app-wide setup such as creating the notification channel used by
 * Firebase Cloud Messaging push notifications (feature #13).
 */
@HiltAndroidApp
class FantasyKingdomApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                "Fantasy Kingdom Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Ride wait times, event announcements and offers"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
