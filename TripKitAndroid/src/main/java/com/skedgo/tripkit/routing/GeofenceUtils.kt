package com.skedgo.tripkit.routing

fun Geofence.toGsmGeofence(): com.google.android.gms.location.Geofence {
    return com.google.android.gms.location.Geofence.Builder()
            .setRequestId(this.id)
            .setCircularRegion(this.center.lat, this.center.lng, this.radius.toFloat())
            .setExpirationDuration(com.google.android.gms.location.Geofence.NEVER_EXPIRE)
            .setTransitionTypes(
                    com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
            )
            .build()
}