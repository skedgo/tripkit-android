package com.skedgo.android.tripkit;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.TripSegment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class TripKitImplTest {
  private TripKit kit;

  @Before public void before() {
    final Configs configs = Configs.builder()
        .context(InstrumentationRegistry.getInstrumentation().getTargetContext())
        .regionEligibility("")
        .debuggable(false)
        .build();
    kit = DaggerTripKit.builder()
        .tripKitModule(new TripKitModule(configs))
        .build();
  }

  @Test public void resolveFlitWaysBooking() {
    final TripSegment segment = new TripSegment();
    segment.setFrom(new Location(33.956252, -118.217896));
    segment.setTo(new Location(33.962775, -118.202395));
    segment.setStartTimeInSecs(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

    final TestSubscriber<BookingAction> subscriber = new TestSubscriber<>();
    kit.getBookingResolver().performExternalActionAsync(
        ExternalActionParams.builder()
            .flitWaysPartnerKey("25251325")
            .action("flitways")
            .segment(segment)
            .build()
    ).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    final List<BookingAction> events = subscriber.getOnNextEvents();
    assertThat(events).hasSize(1);
  }
}