package skedgo.tripkit.routing

import android.content.Context
import com.skedgo.android.common.model.TransportMode
import com.skedgo.android.common.util.TripSegmentUtils
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

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
    append("${segment.getDisplayNotes(context.resources)}\n")
  }
}

private fun StringBuilder.addNewLine() {
  append("\n")
}