package skedgo.tripkit.routing

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