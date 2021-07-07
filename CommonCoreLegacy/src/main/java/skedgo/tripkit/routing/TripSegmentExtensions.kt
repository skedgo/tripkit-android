package skedgo.tripkit.routing

import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
val TripSegment.startDateTime
  get(): DateTime = DateTime(
      TimeUnit.SECONDS.toMillis(startTimeInSecs),
      from.dateTimeZone
  )

/**
 * Get an end date-time with time-zone.
 */
val TripSegment.endDateTime
  get(): DateTime = DateTime(
      TimeUnit.SECONDS.toMillis(endTimeInSecs),
      to.dateTimeZone
  )

val TripSegment.noActionAlerts
  get() =
    alerts?.filter { it.alertAction() == null }

val TripSegment.firstActionAlert
  get() =
    alerts?.firstOrNull { it.alertAction() != null }

val TripSegment.actionAlertsText
  get() =
    alerts?.filter { it.alertAction() != null }?.map { it.text() }.orEmpty().joinToString(separator = "\n")
