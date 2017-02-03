package com.skedgo.android.tripkit.booking.mybookings;

import com.skedgo.android.common.model.BookingConfirmation;
import com.skedgo.android.tripkit.booking.BuildConfig;
import com.skedgo.android.tripkit.booking.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class MyBookingsResponseTest {
  @Test(expected = NullPointerException.class)
  public void bookingsShouldBeNonNull() {
    ImmutableMyBookingsResponse.builder()
        .addBookings((MyBookingsConfirmationResponse) null)
        .build();
  }
}
