package com.skedgo.tripkit.common.agenda

/**
 * Signifies a class is able to fetch timetable information
 */
interface IRealTimeElement {
    var startStopCode: String?
    var endStopCode: String?
    val serviceTripId: String?
    val operator: String?
    val startTimeInSeconds: Long
}