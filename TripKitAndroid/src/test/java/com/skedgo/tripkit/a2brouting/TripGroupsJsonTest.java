package com.skedgo.tripkit.a2brouting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skedgo.tripkit.TripKitAndroidRobolectricTest;

import com.skedgo.tripkit.a2brouting.A2bRoutingApi;
import com.skedgo.tripkit.a2brouting.FailoverA2bRoutingApi;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.core.app.ApplicationProvider;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TripGroupsJsonTest extends TripKitAndroidRobolectricTest {
  private static final Type TRIP_GROUP_TYPE = new TypeToken<List<TripGroup>>() {}.getType();
  @Rule public MockWebServerRule serverRule = new MockWebServerRule();
  private FailoverA2bRoutingApi api;

  @Before public void before() {
    final A2bRoutingApi a2bRoutingApi = new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(serverRule.server.url("/"))
        .build()
        .create(A2bRoutingApi.class);
    api = new FailoverA2bRoutingApi(
        ApplicationProvider.getApplicationContext().getResources(),
        new Gson(),
        a2bRoutingApi
    );
  }

  @Test public void shouldNullifyTripRawSegmentList() throws IOException {
    final MockResponse mockResponse = MockWebServerRule.createMockResponse("/large-routing.json");
    serverRule.server.enqueue(mockResponse);

    final TestObserver<List<TripGroup>> subscriber = api.fetchRoutesAsync(
        Collections.singletonList(serverRule.server.url("/").toString()),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        new HashMap<String, Object>()
    ).test();

    final List<TripGroup> tripGroups = subscriber.values().get(0);
    assertThat(tripGroups).isNotNull().isNotEmpty();

    // Should nullify `rawSegmentList`.
    // Otherwise, gson will convert it to json and result in
    // a very large json which is unnecessary.
    for (TripGroup tripGroup : tripGroups) {
      final List<Trip> trips = tripGroup.getTrips();
      if (trips != null) {
        for (Trip trip : trips) {
          assertThat(trip.rawSegmentList).isNull();
        }
      }
    }

    // Call for sake of benchmark.
    toJson(new Gson(), tripGroups);
  }

  /**
   * We may need to put a breakpoint to check whether
   * the result `json` includes `rawSegmentList` or not.
   */
  private void toJson(Gson gson, List<TripGroup> groups) {
    final long start = System.currentTimeMillis();
    final String json = gson.toJson(groups, TRIP_GROUP_TYPE);
    final long end = System.currentTimeMillis();
    System.out.println("Time: " + (end - start) + "ms");
  }
}