package com.skedgo.android.tripkit;

import android.test.UiThreadTest;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class PeriodicRealTimeTripUpdateReceiverTest {
  @Mock TripUpdater tripUpdater;
  @Mock TripGroup group;
  private RealTimeTripUpdateReceiver receiver;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    receiver = new PeriodicRealTimeTripUpdateReceiverBuilder()
        .tripUpdater(tripUpdater)
        .group(group)
        .initialDelay(1)
        .period(1)
        .timeUnit(TimeUnit.SECONDS)
        .build();
  }

  @UiThreadTest @Test
  public void ignoreErrorByTripUpdater() {
    final Trip displayTrip = mock(Trip.class);
    when(group.getDisplayTrip()).thenReturn(displayTrip);
    when(tripUpdater.getUpdateAsync(eq(displayTrip)))
        .thenReturn(Observable.<Trip>error(new RuntimeException()));

    final TestSubscriber<TripGroup> subscriber = new TestSubscriber<>();
    receiver.startAsync().subscribe(subscriber);
    subscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);
    receiver.stop();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }
}