package com.skedgo.android.tripkit.tsp;

import com.skedgo.android.tripkit.TripKitAndroidRobolectricTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static rx.schedulers.Schedulers.immediate;

public class RegionInfoApiTest extends TripKitAndroidRobolectricTest {
  @Rule public final MockWebServerRule serverRule = new MockWebServerRule();
  private RegionInfoApi api;

  @Before public void before() {
    api = new Retrofit.Builder()
        .baseUrl(serverRule.server.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(immediate()))
        .build()
        .create(RegionInfoApi.class);
  }

  @Test public void successfullyFetchRegionInfo() throws IOException {
    final MockResponse mockResponse = MockWebServerRule.createMockResponse("/regionInfo.json");
    serverRule.server.enqueue(mockResponse);

    final TestSubscriber<RegionInfoResponse> subscriber = new TestSubscriber<>();
    api.fetchRegionInfoAsync(
        "/regionInfo.json",
        ImmutableRegionInfoBody.of("AU_NSW_Sydney")
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    final RegionInfoResponse response = subscriber.getOnNextEvents().get(0);
    assertThat(response.regions()).hasSize(1);
    final RegionInfo regionInfo = response.regions().get(0);
    assertThat(regionInfo.transitWheelchairAccessibility()).isTrue();
  }
}