package com.skedgo.tripkit.routing

/**
 * Use extension functions in [TripExtensionsKt] instead.
 */
@Deprecated("")
object Trips {
    /**
     * Get departure time of a trip null-safely.
     *
     *
     * Use [TripExtensionsKt.startDateTime] instead.
     */
    @Deprecated("")
    fun getDepartureTimezone(trip: Trip?): String? {
        if (trip == null) {
            return null
        }

        val departure = trip.from
        return departure?.timeZone
    }
}