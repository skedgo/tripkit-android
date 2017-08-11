package skedgo.tripkit.routing

import com.skedgo.android.common.model.TransportMode
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
val Trip.startDateTime: DateTime get() = DateTime(
    TimeUnit.SECONDS.toMillis(startTimeInSecs),
    from.dateTimeZone
)

/**
 * Get an end date-time with time-zone.
 */
val Trip.endDateTime: DateTime get() = DateTime(
    TimeUnit.SECONDS.toMillis(endTimeInSecs),
    to.dateTimeZone
)

/**
 * Gets a list of [TripSegment]s visible on the summary area of a [Trip].
 */
fun Trip.getSummarySegments(): List<TripSegment>
    = segments
    ?.filter { it.type != SegmentType.ARRIVAL }
    ?.filter { it.isVisibleInContext(TripSegment.VISIBILITY_IN_SUMMARY) }
    ?: emptyList()

fun Trip.getModeIds(): List<String> =
    segments
        .map { it.transportModeId }
        .filterNotNull()

fun Trip.hasWalkOnly(): Boolean {
  val modeIds = getModeIds()
  return modeIds.size == 1 && modeIds.contains(TransportMode.ID_WALK)
}

fun Trip.getTripSegment(segmentId: Long): TripSegment? = segments?.find { it.id == segmentId }