package com.skedgo.android.tripkit;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import rx.Observable;
import rx.observers.TestSubscriber;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripUpdaterImplTest {
  @Mock Resources resources;
  @Mock TripUpdateApi api;
  private String apiVersion = "11";
  private TripUpdaterImpl updater;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    updater = new TripUpdaterImpl(resources, api, apiVersion, new Gson());
  }

  @Test public void shouldReturnEmpty() {
    final Trip trip = mock(Trip.class);
    final TestSubscriber<Trip> subscriber = new TestSubscriber<>();
    updater.getUpdateAsync(trip).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }

  @Test public void shouldReturnDisplayTrip() {
    final TripGroup group = mock(TripGroup.class);
    final Trip trip = mock(Trip.class);
    final String updateUrl = "https://goo.gl/f2yv7k";
    when(trip.getUpdateURL()).thenReturn(updateUrl);
    final Trip displayTrip = mock(Trip.class);
    when(group.getDisplayTrip()).thenReturn(displayTrip);
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getTripGroupList())
        .thenReturn(new ArrayList<>(singletonList(group)));
    when(api.fetchUpdateAsync(eq(updateUrl), eq(apiVersion)))
        .thenReturn(Observable.just(response));

    final TestSubscriber<Trip> subscriber = new TestSubscriber<>();
    updater.getUpdateAsync(trip).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    assertThat(subscriber.getOnNextEvents()).containsExactly(displayTrip);
  }

  @Test public void shouldReturnEmptyIfNoGroupsAvailable() {
    final Trip trip = mock(Trip.class);
    final String updateUrl = "https://goo.gl/f2yv7k";
    when(trip.getUpdateURL()).thenReturn(updateUrl);
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getTripGroupList()).thenReturn(null);
    when(api.fetchUpdateAsync(eq(updateUrl), eq(apiVersion)))
        .thenReturn(Observable.just(response));

    final TestSubscriber<Trip> subscriber = new TestSubscriber<>();
    updater.getUpdateAsync(trip).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }

  @Test public void shouldReturnEmptyIfNoTripsAvailable() {
    final Trip trip = mock(Trip.class);
    final String updateUrl = "https://goo.gl/f2yv7k";
    when(trip.getUpdateURL()).thenReturn(updateUrl);
    final TripGroup group = mock(TripGroup.class);
    when(group.getDisplayTrip()).thenReturn(null);
    final RoutingResponse response = mock(RoutingResponse.class);
    when(response.getTripGroupList())
        .thenReturn(new ArrayList<>(singletonList(group)));
    when(api.fetchUpdateAsync(eq(updateUrl), eq(apiVersion)))
        .thenReturn(Observable.just(response));

    final TestSubscriber<Trip> subscriber = new TestSubscriber<>();
    updater.getUpdateAsync(trip).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    subscriber.assertNoValues();
  }
}