package com.skedgo.tripkit.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun Context.createNotification(
        channelId: String,
        smallIcon: Int? = null,
        largeIcon: Bitmap? = null,
        contentTitle: String? = null,
        contentText: String? = null,
        groupAlertBehavior: Int? = null,
        groupKey: String? = null,
        groupSummary: Boolean = false,
        sound: Uri? = null,
        autoCancel: Boolean = true,
        bigText: String? = null
): Notification {
    val builder = NotificationCompat.Builder(this, channelId)

    smallIcon?.let { builder.setSmallIcon(it) }
    largeIcon?.let { builder.setLargeIcon(it) }
    contentTitle?.let { builder.setContentTitle(it) }
    contentText?.let { builder.setContentText(it) }
    groupAlertBehavior?.let { builder.setGroupAlertBehavior(it) }
    groupKey?.let { builder.setGroup(it) }
    sound?.let { builder.setSound(it, AudioManager.STREAM_SYSTEM) }
    bigText?.let { builder.setStyle(NotificationCompat.BigTextStyle().bigText(it)) }
    builder.setGroupSummary(groupSummary)
    builder.setAutoCancel(autoCancel)

    return builder.build()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannels(channels: List<NotificationChannel>) {
    val notificationManager = this.getSystemService(NotificationManager::class.java)
    notificationManager?.createNotificationChannels(channels)
}

@RequiresApi(Build.VERSION_CODES.O)
fun createChannel(id: String, name: String): NotificationChannel {
    return NotificationChannel(id, name, NotificationManager.IMPORTANCE_MIN)
}

fun Notification.fire(context: Context, id: Int? = null) {
    NotificationManagerCompat.from(context).notify(id ?: this.hashCode(), this)
}