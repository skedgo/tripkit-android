package com.skedgo.tripkit.routing

import android.content.Context
import com.skedgo.tripkit.common.util.TripSegmentUtils
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import com.skedgo.tripkit.common.model.TransportMode

/**
 * Gets a start date-time with time-zone.
 */
val Trip.startDateTime: DateTime
  get() = DateTime(
      TimeUnit.SECONDS.toMillis(startTimeInSecs),
      from.dateTimeZone
  )

/**
 * Get an end date-time with time-zone.
 */
val Trip.endDateTime: DateTime
  get() = DateTime(
      TimeUnit.SECONDS.toMillis(endTimeInSecs),
      to.dateTimeZone
  )

/**
 * Gets a query date-time with time-zone based on from location
 */
val Trip.queryDateTime: DateTime
  get() = DateTime(
          TimeUnit.SECONDS.toMillis(queryTime),
          from.dateTimeZone
  )

/**
 * Gets a list of [TripSegment]s visible on the summary area of a [Trip].
 */
fun Trip.getSummarySegments(): List<TripSegment> = segments
    ?.filter { it.type != SegmentType.ARRIVAL }
    ?.filter { it.isVisibleInContext(Visibilities.VISIBILITY_IN_SUMMARY) }
    ?: emptyList()

fun Trip.getModeIds(): List<String> =
    segments.mapNotNull { it.transportModeId }

fun Trip.hasWalkOnly(): Boolean {
  val modeIds = getModeIds()
  return modeIds.size == 1 && modeIds.contains(TransportMode.ID_WALK)
}

fun Trip.getTripSegment(segmentId: Long): TripSegment? = segments?.find { it.id == segmentId }

fun Trip.getMainTripSegment(): TripSegment? {
  return this.segments.find { segment -> mainSegmentHashCode == segment.templateHashCode  }
}

fun Trip.getBookingSegment(): TripSegment? {
  var bookingSegment = segments.find { segment -> segment.booking?.quickBookingsUrl != null }
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
      segments?.forEach {
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
        segment.type != SegmentType.STATIONARY &&
        !(segment.from?.isNear(segment.to) ?: false)

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