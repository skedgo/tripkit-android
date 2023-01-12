package com.skedgo.tripkit.routing

import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT

fun Geofence.toGsmGeofence(): com.google.android.gms.location.Geofence {

    val transitionType = if (trigger == Trigger.ENTER.value) {
        GEOFENCE_TRANSITION_ENTER
    } else if (trigger == Trigger.EXIT.value) {
        GEOFENCE_TRANSITION_EXIT
    } else if (trigger.contains(Trigger.ENTER.value) && trigger.contains(Trigger.EXIT.value)) {
        GEOFENCE_TRANSITION_ENTER or GEOFENCE_TRANSITION_EXIT
    } else {
        null
    }

    val geofenceBuilder = com.google.android.gms.location.Geofence.Builder()
            .setRequestId(this.id)
            .setCircularRegion(this.center.lat, this.center.lng, this.radius.toFloat())
            .setExpirationDuration(timeline)
    if (transitionType != null) {
        geofenceBuilder.setTransitionTypes(transitionType)
    }

    return geofenceBuilder.build()
}