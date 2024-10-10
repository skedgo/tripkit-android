package com.skedgo.tripkit.routing

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.model.location.Location
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TripsTest {

    private val trip = mockk<Trip>()

    @Test
    fun departureTimezoneIsNullIfNoDeparture() {
        every { trip.from } returns null
        assertThat(Trips.getDepartureTimezone(trip)).isNull()
    }

    @Test
    fun departureTimezoneIsNullForNullTrip() {
        assertThat(Trips.getDepartureTimezone(null)).isNull()
    }

    @Test
    fun shouldReturnDepartureTimezone() {
        val departure = mockk<Location>()
        every { departure.timeZone } returns "Mars"
        every { trip.from } returns departure
        assertThat(Trips.getDepartureTimezone(trip)).isEqualTo("Mars")
    }
}