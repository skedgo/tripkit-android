package com.skedgo.android.tripkit.tsp

fun RegionInfo.hasWheelChairInformation(): Boolean {
  return this.streetWheelchairAccessibility() || this.transitWheelchairAccessibility()
}