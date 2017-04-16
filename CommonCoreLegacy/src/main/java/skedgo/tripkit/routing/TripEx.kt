package skedgo.tripkit.routing

import com.skedgo.android.common.model.Trip
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

fun Trip.startDateTime(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(startTimeInSecs),
    from.getDateTimeZone()
)

fun Trip.endDateTime(): DateTime = DateTime(
    TimeUnit.SECONDS.toMillis(endTimeInSecs),
    to.getDateTimeZone()
)
