package com.skedgo.tripkit.routing

data class Geofence(
        val id: String,
        val type: String,
        val trigger: String,
        val center: Coordinate,
        val radius: Double,
        val messageType: String,
        val messageTitle: String,
        val messageBody: String
) {
    var timeline: Long = -1L

    fun computeAndSetTimeline(tripEndDateTimeInMillis: Long): Long {
        val currentTimeInMillis = System.currentTimeMillis()
        return tripEndDateTimeInMillis - currentTimeInMillis
    }
}

data class Coordinate(
        val lat: Double,
        val lng: Double
)

enum class Trigger(val value: String) {
    ENTER("ENTER"), EXIT("EXIT")
}

enum class MessageType {
    TRIP_START,
    VEHICLE_IS_APPROACHING,
    ARRIVING_AT_YOUR_STOP,
    NEXT_STOP_IS_YOURS,
    TRIP_END,
}