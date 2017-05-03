package com.skedgo.android.common.model;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.Parcels;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.*;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
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
