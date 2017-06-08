package skedgo.tripkit.a2brouting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skedgo.android.tripkit.BuildConfig;
import com.skedgo.android.tripkit.TestRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.mock;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TripGroupsJsonTest {
  private static final Type TRIP_GROUP_TYPE = new TypeToken<List<TripGroup>>() {}.getType();
  @Rule public MockWebServerRule serverRule = new MockWebServerRule();
  private FailoverA2bRoutingApi api;

  @Before public void before() {
    final A2bRoutingApi a2bRoutingApi = new Retrofit.Builder()
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(serverRule.server.url("/"))
        .build()
        .create(A2bRoutingApi.class);
    api = new FailoverA2bRoutingApi(
        RuntimeEnvironment.application.getResources(),
        new Gson(),
        a2bRoutingApi
    );
  }

  @Test public void shouldNullifyTripRawSegmentList() throws IOException {
    final MockResponse mockResponse = MockWebServerRule.createMockResponse("/large-routing.json");
    serverRule.server.enqueue(mockResponse);

    final TestSubscriber<List<TripGroup>> subscriber = new TestSubscriber<>();
    api.fetchRoutesAsync(
        Collections.singletonList(serverRule.server.url("/").toString()),
        Collections.<String>emptyList(),
        Collections.<String>emptyList(),
        new HashMap<String, Object>()
    ).subscribe(subscriber);

    final List<TripGroup> tripGroups = subscriber.getOnNextEvents().get(0);
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