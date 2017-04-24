package skedgo.tripkit.routing

import com.skedgo.android.common.model.Location
import org.joda.time.DateTimeZone

val Location.dateTimeZone get(): DateTimeZone = timeZone
    ?.let { DateTimeZone.forID(it) }
    ?: DateTimeZone.getDefault()