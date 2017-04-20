package skedgo.tripkit.routing;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.model.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TripsTest {
  private final Trip trip = mock(Trip.class);

  @Test public void departureTimezoneIsNullIfNoDeparture() {
    when(trip.getFrom()).thenReturn(null);
    assertThat(Trips.getDepartureTimezone(trip)).isNull();
  }

  @Test public void departureTimezoneIsNullForNullTrip() {
    assertThat(Trips.getDepartureTimezone(null)).isNull();
  }

  @Test public void shouldReturnDepartureTimezone() {
    final Location departure = mock(Location.class);
    when(departure.getTimeZone()).thenReturn("Mars");
    when(trip.getFrom()).thenReturn(departure);
    assertThat(Trips.getDepartureTimezone(trip)).isEqualTo("Mars");
  }
}