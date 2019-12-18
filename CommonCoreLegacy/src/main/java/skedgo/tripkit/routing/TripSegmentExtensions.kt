package skedgo.tripkit.routing

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
val TripSegment.startDateTime
  get(): DateTime = DateTime(
      TimeUnit.SECONDS.toMillis(startTimeInSecs),
      from?.dateTimeZone ?: DateTimeZone.getDefault()
  )

/**
 * Get an end date-time with time-zone.
 */
val TripSegment.endDateTime
  get(): DateTime = DateTime(
      TimeUnit.SECONDS.toMillis(endTimeInSecs),
      to?.dateTimeZone ?: DateTimeZone.getDefault()
  )

val TripSegment.noActionAlerts
  get() =
    alerts?.filter { it.alertAction() == null }

val TripSegment.actionAlert
  get() =
    alerts?.firstOrNull { it.alertAction() != null }
