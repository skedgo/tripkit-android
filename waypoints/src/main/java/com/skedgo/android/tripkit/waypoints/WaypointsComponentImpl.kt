package com.skedgo.android.tripkit.waypoints

import javax.inject.Inject

class WaypointsComponentImpl @Inject constructor(
        val getTripByChangingStop: GetTripByChangingStop,
        val getTripByChangingService: GetTripByChangingService
) : WaypointsComponent {
    override fun getTripByChangingService(): GetTripByChangingService = getTripByChangingService

    override fun getTripByChangingStop(): GetTripByChangingStop = getTripByChangingStop
}


