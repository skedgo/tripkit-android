package com.skedgo.tripkit.routing

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.google.gson.Gson
import com.skedgo.tripkit.BuildConfig
import com.skedgo.tripkit.R
import com.skedgo.tripkit.extensions.fromJson
import com.skedgo.tripkit.notification.createNotification
import com.skedgo.tripkit.notification.fire

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (BuildConfig.DEBUG)
            Toast.makeText(context, "Geofence update received", Toast.LENGTH_SHORT).show()
        Log.e("GFBroadcastReceiver", "geofence update receive")


        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofences: List<com.skedgo.tripkit.routing.Geofence> =
                    Gson().fromJson(intent.getStringExtra(EXTRA_GEOFENCES) ?: "")
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }

            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                val geofenceId = when {
                    geofencingEvent.triggeringGeofences.isNotEmpty() ->
                        geofencingEvent.triggeringGeofences[0].requestId
                    else -> {
                        Log.e(TAG, "No Geofence Trigger Found.")
                        return
                    }
                }

                val geofence = geofences.first { it.id == geofenceId }
                context.createNotification(
                        channelId = TripAlarmBroadcastReceiver.NOTIFICATION_CHANNEL_START_TRIP_ID,
                        smallIcon = R.drawable.ic_launcher,
                        contentTitle = geofence.messageTitle,
                        contentText = geofence.messageBody,
                        bigText = geofence.messageBody
                ).fire(context)
            }
        }
    }

    private fun errorMessage(context: Context, errorCode: Int): String {
        val resources = context.resources
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> resources.getString(
                    R.string.geofence_not_available
            )
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> resources.getString(
                    R.string.geofence_too_many_geofences
            )
            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> resources.getString(
                    R.string.geofence_too_many_pending_intents
            )
            else -> resources.getString(R.string.unknown_geofence_error)
        }
    }

    companion object {
        const val TAG = "GeofenceReceiver"
        const val ACTION_GEOFENCE_EVENT = "ACTION_GEOFENCE_EVENT"
        const val EXTRA_GEOFENCES = "EXTRA_GEOFENCES"

        fun getPendingIntent(context: Context, bundle: Bundle? = null): PendingIntent {
            val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
            bundle?.let { intent.putExtras(bundle) }
            intent.action = ACTION_GEOFENCE_EVENT
            return PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        }
    }
}