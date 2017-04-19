package skedgo.tripkit.routing

import com.skedgo.android.common.model.Location
import org.joda.time.DateTimeZone

fun Location.getDateTimeZone(): DateTimeZone
    = timeZone
    ?.let { DateTimeZone.forID(it) }
    ?: DateTimeZone.getDefault()