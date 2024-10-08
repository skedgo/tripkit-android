package com.skedgo.tripkit.routing

import android.content.Context
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.util.TripSegmentUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.TimeUnit

val TIME_FORMATTER = DateTimeFormat.forPattern("hh:mm a")

/**
 * Gets a start date-time with time-zone.
 */
val Trip.startDateTime: DateTime
    get() = DateTime(
        TimeUnit.SECONDS.toMillis(startTimeInSecs),
        from?.dateTimeZone
    )

val Trip.startTimeString: String
    get() = startDateTime.toString(TIME_FORMATTER)

/**
 * Get an end date-time with time-zone.
 */
val Trip.endDateTime: DateTime
    get() = DateTime(
        TimeUnit.SECONDS.toMillis(endTimeInSecs),
        to?.dateTimeZone
    )

val Trip.endTimeString: String
    get() = endDateTime.toString(TIME_FORMATTER)

/**
 * Gets a query date-time with time-zone based on from location
 */
val Trip.queryDateTime: DateTime
    get() = DateTime(
        TimeUnit.SECONDS.toMillis(queryTime),
        from?.dateTimeZone
    )

/**
 * Gets a list of [TripSegment]s visible on the summary area of a [Trip].
 */
fun Trip.getSummarySegments(): List<TripSegment> = segmentList
    .filter { it.getType() != SegmentType.ARRIVAL }
    .filter { it.isVisibleInContext(Visibilities.VISIBILITY_IN_SUMMARY) }

fun Trip.getModeIds(): List<String> =
    segmentList.mapNotNull { it.transportModeId }.orEmpty()

fun Trip.hasWalkOnly(): Boolean {
    val modeIds = getModeIds()
    return modeIds.size == 1 && modeIds.contains(TransportMode.ID_WALK)
}

fun Trip.getTripSegment(segmentId: Long): TripSegment? = segmentList?.find { it.segmentId == segmentId }

fun Trip.getMainTripSegment(): TripSegment? {
    return this.segmentList?.find { segment -> mainSegmentHashCode == segment.templateHashCode }
}

fun Trip.getBookingSegment(): TripSegment? {
    var bookingSegment = segmentList?.find { segment -> segment.booking?.quickBookingsUrl != null }
    if (bookingSegment == null) {
        val mainSegment = getMainTripSegment()
        if (mainSegment != null && mainSegment.miniInstruction != null && mainSegment.miniInstruction.instruction != null) {
            bookingSegment = mainSegment
        }
    }

    return bookingSegment
}

fun Trip.constructPlainText(context: Context): String =
    StringBuilder().apply {
        segmentList?.forEach {
            addAddress(it)
            addSegmentAction(context, it)
            addNotes(context, it)
            addNewLine()
        }
    }.toString()

private fun StringBuilder.addAddress(segment: TripSegment) {
    if (shouldAdAddress(segment)) {
        append("${segment.from?.address}, ")
    }
}

private fun shouldAdAddress(segment: TripSegment) =
    !segment.from?.address.isNullOrEmpty() &&
        segment.getType() != SegmentType.STATIONARY &&
        !(segment.to?.run { segment.from?.isNear(this) ?: false } ?: false)

private fun StringBuilder.addSegmentAction(context: Context, segment: TripSegment) {
    append("${TripSegmentUtils.getTripSegmentAction(context, segment)}\n")
}

private fun StringBuilder.addNotes(context: Context, segment: TripSegment) {
    if (!segment.notes.isNullOrEmpty()) {
        append("${segment.getDisplayNotes(context, true)}\n")
    }
}

private fun StringBuilder.addNewLine() {
    append("\n")
}