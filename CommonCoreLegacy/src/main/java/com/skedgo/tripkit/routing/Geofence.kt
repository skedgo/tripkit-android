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
)

data class Coordinate(
        val lat: Double,
        val lng: Double
)

enum class Trigger {
    ENTER, EXIT
}

enum class MessageType {
    TRIP_END, ARRIVING_AT_YOUR_STOP, NEXT_STOP_IS_YOURS
}