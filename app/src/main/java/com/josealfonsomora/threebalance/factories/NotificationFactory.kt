package com.josealfonsomora.threebalance.factories

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "channel_three"
const val NOTIFICATION_ID = 1234

class NotificationFactory {
    fun newNotification(
        title: String,
        content: String,
        largeContent: String,
        context: Context,
        notificationChannel: String
    ): Notification {
        return NotificationCompat.Builder(context, notificationChannel)
            .setContentTitle(title)
            .setSmallIcon(android.R.drawable.sym_def_app_icon)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(largeContent))
            .setChannelId(CHANNEL_ID)
            .build()
    }

}
