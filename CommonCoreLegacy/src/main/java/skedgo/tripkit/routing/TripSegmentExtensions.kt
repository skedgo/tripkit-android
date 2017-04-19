package skedgo.tripkit.routing

import com.skedgo.android.common.model.TripSegment
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
fun TripSegment.startDateTime(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(startTimeInSecs),
    from.getDateTimeZone()
)

/**
 * Get an end date-time with time-zone.
 */
fun TripSegment.endDateTime(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(endTimeInSecs),
    to.getDateTimeZone()
)
