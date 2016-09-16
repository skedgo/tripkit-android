package com.skedgo.android.common.model;

import android.test.AndroidTestCase;

import com.skedgo.android.common.util.MockResponseInterceptor;
import com.skedgo.android.common.util.TripSegmentListResolver;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;

public class TripTest2 extends AndroidTestCase {
  public void testUpdateTripPeriodically() throws InterruptedException, IOException {
    OkHttpClient httpClient = new OkHttpClient();
    httpClient.interceptors().add(createMockResponseInterceptor("updateTrip0.json"));

    Trip trip = new Trip();
    trip.setUpdateURL("http://granduni.buzzhives.com/satapp/updateTrip");

    TripSegment segment0 = new TripSegment();
    segment0.setTemplateHashCode(70391450);
    segment0.setAction("Take MRT");

    TripSegment segment1 = new TripSegment();
    segment1.setTemplateHashCode(-1927118844);
    segment1.setAction("Ride scooter");

    trip.setSegments(new ArrayList<>(Arrays.asList(
        segment0,
        segment1
    )));

    new TripSegmentListResolver(getContext().getResources())
        .setOrigin(new Location())
        .setDestination(new Location())
        .setTripSegmentList(trip.getSegments())
        .resolve();

    trip.updateTripPeriodically(getContext().getResources(), httpClient, 2, TimeUnit.SECONDS)
        .toBlocking().first();

    assertThat(trip.getStartTimeInSecs()).isEqualTo(1422614938);
    assertThat(trip.getEndTimeInSecs()).isEqualTo(1422616904);
    assertThat(trip.getUpdateURL()).isEqualTo("http://granduni.buzzhives.com/satapp/updateTrip?ID=1aba7011-521a-4be2-b65c-9e545ad36268");
    assertThat(trip.getProgressURL()).isEqualTo("http://granduni.buzzhives.com/satapp/postTripProgress?ID=1aba7011-521a-4be2-b65c-9e545ad36268");
    assertThat(trip.getSaveURL()).isEqualTo("http://granduni.buzzhives.com/satapp/persistTrip?ID=1aba7011-521a-4be2-b65c-9e545ad36268");
    assertThat(trip.getCarbonCost()).isEqualTo(1.5f);
    assertThat(trip.getMoneyCost()).isEqualTo(7.99f);
    assertThat(trip.getHassleCost()).isEqualTo(0f);

    TripSegment departureSegment = trip.getSegments().get(0);
    assertThat(departureSegment.getStartTimeInSecs())
        .describedAs("Departure time must be updated by realtime data")
        .isEqualTo(1422614998L);
    assertThat(departureSegment.getEndTimeInSecs())
        .describedAs("Departure time must be updated by realtime data")
        .isEqualTo(1422614998L);

    assertThat(segment0.getStartTimeInSecs()).isEqualTo(1422614998L);
    assertThat(segment0.getEndTimeInSecs()).isEqualTo(1422616153L);
    assertThat(segment0.isRealTime()).isTrue();

    assertThat(segment1.getStartTimeInSecs()).isEqualTo(1422616153L);
    assertThat(segment1.getEndTimeInSecs()).isEqualTo(1422616393L);
    assertThat(segment1.isRealTime()).isFalse();

    TripSegment arrivalSegment = trip.getSegments().get(trip.getSegments().size() - 1);
    assertThat(arrivalSegment.getStartTimeInSecs())
        .describedAs("Arrival time must be updated by realtime data")
        .isEqualTo(1422616393L);
    assertThat(arrivalSegment.getEndTimeInSecs())
        .describedAs("Arrival time must be updated by realtime data")
        .isEqualTo(1422616393L);
  }

  public void testNotCrashWithBadUpdate() throws InterruptedException, IOException {
    // updateTrip1.json is a valid JSON response but it contains no trip group.
    OkHttpClient httpClient = new OkHttpClient();
    httpClient.interceptors().add(createMockResponseInterceptor("updateTrip1.json"));

    final CountDownLatch counter = new CountDownLatch(1);

    Trip trip = new Trip();
    trip.setUpdateURL("http://granduni.buzzhives.com/satapp/updateTrip");

    try {
      trip.updateTripPeriodically(getContext().getResources(), httpClient, 10, TimeUnit.SECONDS)
          .subscribe(new Action1<Trip>() {
            @Override
            public void call(Trip trip) {
              counter.countDown();
            }
          });

      // Why false? With bad data, it's expected not to emit update.
      assertFalse(counter.await(5, TimeUnit.SECONDS));
    } catch (Exception e) {
      Assertions.fail("Failed to deal with bad data", e);
    }
  }

  public void testNotCrashWithMalformedResponse() throws InterruptedException, IOException {
    // updateTrip2.json is a malformed JSON.
    OkHttpClient httpClient = new OkHttpClient();
    httpClient.interceptors().add(createMockResponseInterceptor("updateTrip2.json"));

    final CountDownLatch counter = new CountDownLatch(1);

    Trip trip = new Trip();
    trip.setUpdateURL("http://granduni.buzzhives.com/satapp/updateTrip");

    try {
      trip.updateTripPeriodically(getContext().getResources(), httpClient, 10, TimeUnit.SECONDS)
          .subscribe(new Action1<Trip>() {
            @Override
            public void call(Trip trip) {
              counter.countDown();
            }
          });

      // Why false? With bad data, it's expected not to emit update.
      assertFalse(counter.await(5, TimeUnit.SECONDS));
    } catch (Exception e) {
      Assertions.fail("Failed to deal with bad data", e);
    }
  }

  private Interceptor createMockResponseInterceptor(final String fileName) {
    return MockResponseInterceptor.create(
        getContext(),
        fileName
    );
  }
}