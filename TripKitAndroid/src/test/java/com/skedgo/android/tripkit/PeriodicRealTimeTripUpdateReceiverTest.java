package com.skedgo.android.tripkit;

import android.test.UiThreadTest;

import kotlin.Pair;
import rx.schedulers.Schedulers;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    when(displayTrip.getUpdateURL()).thenReturn("AUG 2016");
    when(group.getDisplayTrip()).thenReturn(displayTrip);
    when(tripUpdater.getUpdateAsync("AUG 2016"))
        .thenReturn(Observable.<Trip>error(new RuntimeException()));

    final TestSubscriber<Pair<Trip, TripGroup>> subscriber = new TestSubscriber<>();
    receiver.startAsync().subscribe(subscriber);
    subscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);
    receiver.stop();
    subscriber.assertTerminalEvent();
    subscriber.assertNoErrors();
  }
}