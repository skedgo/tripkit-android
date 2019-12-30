package com.skedgo.tripkit.routing

import java.util.*

enum class SegmentType {
  DEPARTURE, SCHEDULED, UNSCHEDULED, STATIONARY, ARRIVAL
}

fun String?.from(): SegmentType? = SegmentType.values().firstOrNull {
  it.name.toLowerCase(Locale.US) == this?.toLowerCase(Locale.US)
}
