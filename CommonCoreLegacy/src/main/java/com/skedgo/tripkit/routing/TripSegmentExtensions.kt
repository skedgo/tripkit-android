package com.skedgo.tripkit.routing

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Gets a start date-time with time-zone.
 */
val TripSegment.startDateTime
    get(): DateTime = DateTime(
        TimeUnit.SECONDS.toMillis(startTimeInSecs),
        from?.dateTimeZone ?: DateTimeZone.getDefault()
    )

val TripSegment.startDateTimeCalendar
    get(): Calendar = this.startDateTime.toGregorianCalendar()

val TripSegment.timetableStartDateTime
    get(): DateTime = DateTime(
        TimeUnit.SECONDS.toMillis(timetableStartTime),
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

val TripSegment.endDateTimeCalendar
    get(): Calendar = this.endDateTime.toGregorianCalendar()

val TripSegment.timetableEndDateTime
    get(): DateTime = DateTime(
        TimeUnit.SECONDS.toMillis(timetableEndTime),
        to?.dateTimeZone ?: DateTimeZone.getDefault()
    )

val TripSegment.noActionAlerts
    get() =
        alerts?.filter { it.alertAction() == null }

val TripSegment.actionAlert
    get() =
        alerts?.firstOrNull { it.alertAction() != null }

val TripSegment?.bookingHasConfirmation
    get() = this?.booking?.confirmation?.status() != null