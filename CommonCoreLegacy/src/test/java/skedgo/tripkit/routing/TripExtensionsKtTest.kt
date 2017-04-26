package skedgo.tripkit.routing

import com.skedgo.android.common.model.Location
import org.assertj.core.api.Java6Assertions.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

class TripExtensionsKtTest {
  @Test fun shouldReturnZonedStartDateTimeWithDepartureLocationTimeZone() {
    val departureLocation = Location()
    departureLocation.timeZone = "Asia/Bangkok"

    val segment = TripSegment()
    segment.from = departureLocation

    val trip = Trip()
    trip.segments = arrayListOf(segment)
    trip.startTimeInSecs = DateTime.parse("2012-06-30T00:00").toSeconds()

    assertThat(trip.startDateTime).isEqualTo(
        DateTime.parse("2012-06-30T00:00")
            .withZone(DateTimeZone.forID("Asia/Bangkok"))
    )
  }

  @Test fun shouldReturnZonedEndDateTimeWithArrivalLocationTimeZone() {
    val arrival = Location()
    arrival.timeZone = "Asia/Bangkok"

    val segment = TripSegment()
    segment.to = arrival

    val trip = Trip()
    trip.segments = arrayListOf(segment)
    trip.endTimeInSecs = DateTime.parse("2012-06-30T00:00").toSeconds()

    assertThat(trip.endDateTime).isEqualTo(
        DateTime.parse("2012-06-30T00:00")
            .withZone(DateTimeZone.forID("Asia/Bangkok"))
    )
  }
}
