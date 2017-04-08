package skedgo.tripkit.routing.a2b

import org.joda.time.DateTime

sealed class RequestTime {
  data class DepartAfter(val dateTime: DateTime) : RequestTime()
  data class ArriveBefore(val dateTime: DateTime) : RequestTime()
  data class DepartNow(val getNow: () -> DateTime) : RequestTime()
}
