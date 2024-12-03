package com.skedgo.tripkit

class OutOfRegionsException(
    detailMessage: String?,
    private val latitude: Double,
    private val longitude: Double
) : RuntimeException(detailMessage) {
    fun latitude(): Double {
        return latitude
    }

    fun longitude(): Double {
        return longitude
    }
}
