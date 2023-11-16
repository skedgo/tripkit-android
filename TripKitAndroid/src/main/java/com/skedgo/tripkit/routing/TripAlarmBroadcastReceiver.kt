package com.skedgo.tripkit.routing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.skedgo.tripkit.R
import com.skedgo.tripkit.extensions.fromJson
import com.skedgo.tripkit.notification.createNotification
import com.skedgo.tripkit.notification.fire
import org.joda.time.format.DateTimeFormat

class TripAlarmBroadcastReceiver : BroadcastReceiver() {

    private val gson = Gson()

    override fun onReceive(context: Context, intent: Intent) {
        intent.extras?.let { extras ->
            if (extras.containsKey(ACTION_START_TRIP_EVENT)) {
                val trip: Trip = gson.fromJson(extras.getString(EXTRA_START_TRIP_EVENT_TRIP, ""))
                trip.segments.minByOrNull { it.startTimeInSecs }?.let { startSegment ->
                    context.createNotification(
                            channelId = NOTIFICATION_CHANNEL_START_TRIP_ID,
                            smallIcon = R.drawable.ic_launcher,
                            contentTitle = "Trip about to start",
                            contentText = "${startSegment.startDateTime.toString(DateTimeFormat.forPattern("HH:mm a"))} ${startSegment.serviceName ?: startSegment.serviceDirection ?: startSegment.action}",
                            bigText = "${startSegment.startDateTime.toString(DateTimeFormat.forPattern("HH:mm a"))} ${startSegment.serviceName ?: startSegment.serviceDirection ?: startSegment.action}"
                    ).fire(context)
                }

            }
        }
    }

    companion object {
        const val ACTION_START_TRIP_EVENT = "ACTION_START_TRIP_EVENT"
        const val EXTRA_START_TRIP_EVENT_TRIP = "EXTRA_START_TRIP_EVENT_TRIP"
        const val NOTIFICATION_CHANNEL_START_TRIP_ID = "NOTIFICATION_CHANNEL_START_TRIP_ID"
        const val NOTIFICATION_CHANNEL_START_TRIP = "NOTIFICATION_CHANNEL_START_TRIP"
    }
}