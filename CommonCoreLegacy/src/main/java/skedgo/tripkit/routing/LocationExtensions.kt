package skedgo.tripkit.routing

import com.skedgo.android.common.model.Location
import org.joda.time.DateTimeZone
import java.lang.Math.abs

const val SimilarLocationDegreeDifference = 0.000025

val Location.dateTimeZone get(): DateTimeZone = timeZone
    ?.let { DateTimeZone.forID(it) }
    ?: DateTimeZone.getDefault()

// https://github.com/skedgo/tripkit-ios/blob/master/TripKit/Classes/core/RootKit/Model/SGLocationHelper.m#L240
fun Location.isNear(location: Location) : Boolean {
  val latDiff = lat - location.lat
  val lngDiff = lon - location.lon
  val maxDiff = SimilarLocationDegreeDifference * 2
  return abs(latDiff) < maxDiff && abs(lngDiff) < maxDiff
}
