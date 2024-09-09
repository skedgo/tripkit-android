package com.skedgo.tripkit.tsp

import com.skedgo.tripkit.data.tsp.RegionInfo

fun RegionInfo.hasWheelChairInformation(): Boolean {
    return this.streetWheelchairAccessibility() || this.transitWheelchairAccessibility()
}