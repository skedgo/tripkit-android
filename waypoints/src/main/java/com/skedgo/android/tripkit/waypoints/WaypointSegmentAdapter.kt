package com.skedgo.android.tripkit.waypoints

class WaypointSegmentAdapter(
        val start: String,
        val end: String,
        val modes: List<String>,
        val startTime: Int = 0,
        val endTime: Int = 0,
        val serviceTripId: String = "",
        val operator: String = "",
        val region: String = ""
)