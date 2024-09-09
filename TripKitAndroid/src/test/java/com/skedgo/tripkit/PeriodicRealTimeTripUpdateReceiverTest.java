package com.skedgo.tripkit;

import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subscribers.TestSubscriber;
import kotlin.Pair;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PeriodicRealTimeTripUpdateReceiverTest {
    @Mock
    TripUpdater tripUpdater;
    @Mock
    TripGroup group;
    private RealTimeTripUpdateReceiver receiver;

    @Before
    public void before() {
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

        final TestSubscriber<Pair<Trip, TripGroup>> subscriber = receiver.startAsync().test();
        subscriber.awaitTerminalEvent(3, TimeUnit.SECONDS);
        receiver.stop();
        subscriber.assertTerminated();
        subscriber.assertNoErrors();
    }
}