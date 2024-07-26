package com.skedgo.tripkit.routing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.skedgo.tripkit.R
import com.skedgo.tripkit.extensions.fromJson
import com.skedgo.tripkit.notification.createNotification
import com.skedgo.tripkit.notification.fire
import org.joda.time.format.DateTimeFormat

private const val DATE_TIME_FORMAT = "HH:mm a"

class TripAlarmBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_START_TRIP_EVENT = "ACTION_START_TRIP_EVENT"
        const val EXTRA_START_TRIP_EVENT_TRIP = "EXTRA_START_TRIP_EVENT_TRIP"
        const val EXTRA_START_TRIP_EVENT_TRIP_GROUP_UUID = "EXTRA_START_TRIP_EVENT_TRIP_GROUP_UUID"
        const val NOTIFICATION_CHANNEL_START_TRIP_ID = "NOTIFICATION_CHANNEL_START_TRIP_ID"
        const val NOTIFICATION_CHANNEL_START_TRIP = "NOTIFICATION_CHANNEL_START_TRIP"
        const val NOTIFICATION_TRIP_START_NOTIFICATION_ID = 9001
    }

    private val gson = Gson()

    override fun onReceive(context: Context, intent: Intent) {
        intent.extras?.let { extras ->
            if (extras.containsKey(ACTION_START_TRIP_EVENT)) {
                val tripString = extras.getString(EXTRA_START_TRIP_EVENT_TRIP, "")
                val tripGroupUuid = extras.getString(EXTRA_START_TRIP_EVENT_TRIP_GROUP_UUID, "")
                val trip: Trip = gson.fromJson(tripString)
                trip.segments.minByOrNull { it.startTimeInSecs }?.let { startSegment ->

                    context.createNotification(
                        channelId = NOTIFICATION_CHANNEL_START_TRIP_ID,
                        smallIcon = R.drawable.ic_launcher,
                        contentTitle = context.getString(R.string.trip_about_to_start),
                        contentText = "${
                            startSegment.startDateTime.toString(
                                DateTimeFormat.forPattern(
                                    DATE_TIME_FORMAT
                                )
                            )
                        } ${startSegment.serviceName ?: startSegment.serviceDirection ?: startSegment.action}",
                        bigText = "${
                            startSegment.startDateTime.toString(
                                DateTimeFormat.forPattern(
                                    DATE_TIME_FORMAT
                                )
                            )
                        } ${startSegment.serviceName ?: startSegment.serviceDirection ?: startSegment.action}",
                        intent = Intent("com.skedgo.tripgo.APP_NOTIFICATION").apply {
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            putExtra(EXTRA_START_TRIP_EVENT_TRIP, tripString)
                            putExtra(EXTRA_START_TRIP_EVENT_TRIP_GROUP_UUID, tripGroupUuid)
                        },
                        priority = NotificationCompat.PRIORITY_HIGH
                    ).fire(context, NOTIFICATION_TRIP_START_NOTIFICATION_ID)
                }
            }
        }
    }
}