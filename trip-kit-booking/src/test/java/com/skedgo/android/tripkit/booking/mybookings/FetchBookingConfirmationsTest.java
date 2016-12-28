package com.skedgo.android.tripkit.booking.mybookings;

import com.skedgo.android.common.model.BookingConfirmation;
import com.skedgo.android.tripkit.booking.BuildConfig;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class FetchBookingConfirmationsTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock MyBookingsApi api;
  private FetchBookingConfirmations useCase;

  @Before public void setUp() {
    useCase = new FetchBookingConfirmations(api);
  }

  @Test public void shouldExtractBookingConfirmations() {
    final MyBookingsResponse response = mock(MyBookingsResponse.class);
    final List<BookingConfirmation> expected = Arrays.asList(
        mock(BookingConfirmation.class),
        mock(BookingConfirmation.class)
    );
    when(response.bookings()).thenReturn(expected);
    when(api.fetchMyBookingsAsync(eq(0), eq(3)))
        .thenReturn(Observable.just(response));

    final List<BookingConfirmation> actual = useCase.call(0, 3).toBlocking().first();
    assertThat(actual).isSameAs(expected);
  }
}
