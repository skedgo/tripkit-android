package com.skedgo.android.tripkit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import kotlin.Pair;
import rx.Observable;
import rx.observers.TestSubscriber;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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

  @Test
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