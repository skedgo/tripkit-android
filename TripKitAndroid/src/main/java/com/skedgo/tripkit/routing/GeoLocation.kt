package com.skedgo.tripkit.routing

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.skedgo.tripkit.BuildConfig


@SuppressLint("StaticFieldLeak")
object GeoLocation {

    val TAG = GeoLocation::class.java.simpleName
    lateinit var context: Context
    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(context)
    }
    private val currentGeofenceList = mutableListOf<com.google.android.gms.location.Geofence>()
    private var geofencePendingIntent: PendingIntent? = null

    fun init(context: Context) {
        this.context = context
    }

    @SuppressLint("MissingPermission")
    fun createGeoFences(geofences: List<Geofence>) {
        val gsmGeofences = mutableListOf<com.google.android.gms.location.Geofence>()
        gsmGeofences.addAll(
                geofences.map { it.toGsmGeofence() }
        )

        currentGeofenceList.clear()
        currentGeofenceList.addAll(gsmGeofences)

        val bundle = Bundle()
        bundle.putString(GeofenceBroadcastReceiver.EXTRA_GEOFENCES, Gson().toJson(geofences))
        geofencePendingIntent = GeofenceBroadcastReceiver.getPendingIntent(context, bundle)

        createGeoFencingRequest()?.let { request ->
            if (currentGeofenceList.isNotEmpty()) {
                removeGeoFences(onRemoveCallback = {
                    geofencingClient.addGeofences(request, geofencePendingIntent).run {
                        addOnSuccessListener {
                            Log.e(TAG, "Geofence added successfully")
                        }
                        addOnFailureListener {
                            Log.e(TAG, "Geofence add failed: ${it.message}")
                            currentGeofenceList.clear()
                            if (BuildConfig.DEBUG)
                                it.printStackTrace()
                        }
                    }
                })
            }
        }
    }

    private fun createGeoFencingRequest(): GeofencingRequest? {
        if (currentGeofenceList.isNotEmpty()) {
            Log.e("GeoLocation", "creating geofence with ${currentGeofenceList.size} features")
            return GeofencingRequest.Builder().apply {
                setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                addGeofences(currentGeofenceList)
            }.build()
        }

        return null
    }

    fun clearGeofences() {
        currentGeofenceList.clear()
        if (geofencePendingIntent != null) {
            removeGeoFences()
        }
    }

    private fun removeGeoFences(onRemoveCallback: (() -> Unit)? = null) {
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                Log.e(TAG, "Remove geofence success")
                onRemoveCallback?.invoke()
            }
            addOnFailureListener {
                Log.e(TAG, "Remove geofence failed")
                onRemoveCallback?.invoke()
                if (BuildConfig.DEBUG)
                    it.printStackTrace()
            }
        }
    }

}