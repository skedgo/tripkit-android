package com.skedgo.tripkit.parkingspots.models

import com.skedgo.tripkit.utils.OptionalCompat

open class ParkingOperator(
    val name: String,
    val phone: OptionalCompat<String>,
    val website: OptionalCompat<String>
) {
}