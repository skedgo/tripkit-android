package com.skedgo.tripkit.routing

enum class Availability(val value: String) {
    Available("AVAILABLE"),
    MissedPrebookingWindow("MISSED_PREBOOKING_WINDOW"),
    Cancelled("CANCELLED");
}

fun String?.toAvailability(): Availability? = this?.let {
    Availability.values().first { it.value == this }
}
