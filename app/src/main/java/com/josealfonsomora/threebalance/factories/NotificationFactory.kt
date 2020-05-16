package com.josealfonsomora.threebalance.factories

import android.app.Notification
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat

class NotificationFactory {

    companion object {
        const val CHANNEL_ID = "channel_three"
        const val NOTIFICATION_ID = 1234

        fun newSimpleNotification(title: String, content: String, context: Context, notificationChannel: String): Notification {
            return NotificationCompat.Builder(context, notificationChannel)
                .setContentTitle(title)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText(content)
                .setChannelId(CHANNEL_ID)
                .build()
        }

        fun newNotificationWithTimeOut(
            context: Context,
            notificationChannel: String,
            timeout: Long
        ): Notification {
            return NotificationCompat.Builder(context, notificationChannel)
                .setContentTitle("Notification Title")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText("This notification will be destroyed in %s".format(timeout.toString()))
                .setTimeoutAfter(timeout * 1000).build()
        }

        fun newColorizedNotification(context: Context, notificationChannel: String): Notification {
            return NotificationCompat.Builder(context, notificationChannel)
                .setContentTitle("Notification title")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentText("Notification text")
                .setColorized(true)
                .setColor(Color.RED)
                .setOngoing(true)
                .build()
        }
    }

}
