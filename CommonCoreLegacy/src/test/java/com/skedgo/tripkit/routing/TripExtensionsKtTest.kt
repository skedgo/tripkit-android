package com.skedgo.tripkit.routing

import android.content.Context
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.mock
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.common.model.Location
import org.amshove.kluent.When
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.assertj.core.api.Java6Assertions.assertThat
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Test

class TripExtensionsKtTest {
    @Test
    fun shouldReturnZonedStartDateTimeWithDepartureLocationTimeZone() {
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

    @Test
    fun shouldReturnZonedEndDateTimeWithArrivalLocationTimeZone() {
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

    @Test
    fun `should create segments plain text`() {
        // Arrange.
        val context: Context = mock()

        val from = Location()
        val to = Location()

        val segment = TripSegment()
        segment.from = from
        segment.to = to
        segment.action = "ACTION"

        val trip = Trip()
        trip.segments = arrayListOf(segment)

        // Act.
        val text = trip.constructPlainText(context)

        // Assert.
        text `should be equal to` "ACTION\n\n"
    }

    @Test
    fun `should create segments plain text with address`() {
        // Arrange.
        val context: Context = mock()

        val from = Location()
        from.lat = 100.0
        from.lon = 100.0
        val to = Location()
        to.lat = 10.0
        to.lon = 10.0

        from.address = "A"

        val segment = TripSegment()
        segment.from = from
        segment.to = to
        segment.action = "ACTION"

        val trip = Trip()
        trip.segments = arrayListOf(segment)

        // Act.
        val text = trip.constructPlainText(context)

        // Assert.
        text `should be equal to` "A, ACTION\n\n"
    }

    /*
    @Test
    fun `should create segments plain text with notes` () {
      // Arrange.
      val context: Context = mock()
      val resources: Resources = mock()
      When calling context.resources itReturns resources
      When calling resources.getString(R.string.for__pattern) itReturns "for %1\$s"

      val from = Location()
      val to = Location()

      val segment = TripSegment()
      segment.from = from
      segment.to = to
      segment.action = "ACTION<DURATION>"
      segment.startTimeInSecs = 0
      segment.endTimeInSecs = 120

      val trip = Trip()
      trip.segments = arrayListOf(segment)

      // Act.
      val text = trip.constructPlainText(context)

      // Assert.
      text `should be equal to` "ACTION for 2mins\n\n"
    }
    */

    @Test
    fun `should create segments plain text with null locations`() {
        // Arrange.
        val context: Context = mock()

        val segment = TripSegment()
        segment.action = "ACTION"

        val trip = Trip()
        trip.segments = arrayListOf(segment)

        // Act.
        val text = trip.constructPlainText(context)

        // Assert.
        text `should be equal to` "ACTION\n\n"
    }
}
