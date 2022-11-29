package com.skedgo.tripkit.routing

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.skedgo.tripkit.BuildConfig


@SuppressLint("StaticFieldLeak")
object GeoLocation {

    val TAG = GeoLocation::class.java.simpleName
    lateinit var context: Context
    lateinit var geofencingClient: GeofencingClient
    private val currentGeofenceList = mutableListOf<com.google.android.gms.location.Geofence>()
    private val geofencePendingIntent: PendingIntent by lazy {
        GeofenceBroadcastReceiver.getPendingIntent(context)
    }

    fun init(context: Context) {
        this.context = context
    }

    @SuppressLint("MissingPermission")
    fun createGeoFences(geofences: List<Geofence>) {
        val gsmGeofences = mutableListOf<com.google.android.gms.location.Geofence>()
        gsmGeofences.addAll(geofences.map { it.toGsmGeofence() })

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