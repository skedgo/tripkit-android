package com.skedgo.tripkit.common.model;

import com.skedgo.tripkit.common.Parcels;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class BookingConfirmationTest {
  @Test public void parcel() {
    final BookingConfirmation expected = ImmutableBookingConfirmation.builder()
        .status(
            ImmutableBookingConfirmationStatus.builder()
                .title("Some title")
                .build()
        )
        .addActions(
            ImmutableBookingConfirmationAction.builder()
                .title("Some title")
                .type(BookingConfirmationAction.TYPE_CALL)
                .isDestructive(false)
                .build()
        )
        .build();
    final BookingConfirmation actual = BookingConfirmation.CREATOR.createFromParcel(
        Parcels.parcel(expected)
    );
    assertThat(actual).isEqualTo(expected);
  }
}
