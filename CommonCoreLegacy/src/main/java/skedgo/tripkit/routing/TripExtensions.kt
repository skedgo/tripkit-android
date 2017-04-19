package skedgo.tripkit.routing

import com.skedgo.android.common.model.SegmentType
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripSegment
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
fun Trip.startDateTime(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(startTimeInSecs),
    from.getDateTimeZone()
)

/**
 * Get an end date-time with time-zone.
 */
fun Trip.endDateTime(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(endTimeInSecs),
    to.getDateTimeZone()
)

/**
 * Gets a list of [TripSegment]s visible on the summary area of a [Trip].
 */
fun Trip.getSummarySegments(): List<TripSegment>
    = segments
    ?.filter { it.type != SegmentType.ARRIVAL }
    ?.filter { it.isVisibleInContext(TripSegment.VISIBILITY_IN_SUMMARY) }
    ?: emptyList()