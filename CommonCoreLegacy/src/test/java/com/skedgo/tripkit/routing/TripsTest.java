package com.skedgo.tripkit.routing;

import com.skedgo.tripkit.common.model.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class TripsTest {
    private final Trip trip = mock(Trip.class);

    @Test
    public void departureTimezoneIsNullIfNoDeparture() {
        when(trip.getFrom()).thenReturn(null);
        assertThat(Trips.getDepartureTimezone(trip)).isNull();
    }

    @Test
    public void departureTimezoneIsNullForNullTrip() {
        assertThat(Trips.getDepartureTimezone(null)).isNull();
    }

    @Test
    public void shouldReturnDepartureTimezone() {
        final Location departure = mock(Location.class);
        when(departure.getTimeZone()).thenReturn("Mars");
        when(trip.getFrom()).thenReturn(departure);
        assertThat(Trips.getDepartureTimezone(trip)).isEqualTo("Mars");
    }
}