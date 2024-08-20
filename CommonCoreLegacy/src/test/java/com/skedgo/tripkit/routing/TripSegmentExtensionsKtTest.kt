package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.model.Location
import org.assertj.core.api.Java6Assertions.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripSegmentExtensionsKtTest {
    @Test
    fun shouldReturnZonedStartDateTimeWithDepartureLocationTimeZone() {
        val departureLocation = Location()
        departureLocation.timeZone = "Asia/Bangkok"

        val segment = TripSegment()
        segment.from = departureLocation
        segment.startTimeInSecs = DateTime.parse("2012-06-30T00:00").toSeconds()

        assertThat(segment.startDateTime).isEqualTo(
            DateTime.parse("2012-06-30T00:00")
                .withZone(DateTimeZone.forID("Asia/Bangkok"))
        )
    }

    @Test
    fun shouldReturnZonedEndDateTimeWithArrivalLocationTimeZone() {
        val arrival = Location()
        arrival.timeZone = "Asia/Bangkok"

        val segment = TripSegment()
        segment.to = arrival
        segment.endTimeInSecs = DateTime.parse("2012-06-30T00:00").toSeconds()

        assertThat(segment.endDateTime).isEqualTo(
            DateTime.parse("2012-06-30T00:00")
                .withZone(DateTimeZone.forID("Asia/Bangkok"))
        )
    }
}
