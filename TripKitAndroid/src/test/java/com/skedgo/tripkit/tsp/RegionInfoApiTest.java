package com.skedgo.tripkit.tsp;

import com.skedgo.tripkit.TripKitAndroidRobolectricTest;

import com.skedgo.tripkit.data.tsp.RegionInfo;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static io.reactivex.schedulers.Schedulers.trampoline;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class RegionInfoApiTest extends TripKitAndroidRobolectricTest {
  @Rule public final MockWebServerRule serverRule = new MockWebServerRule();
  private RegionInfoApi api;

  @Before public void before() {
    api = new Retrofit.Builder()
        .baseUrl(serverRule.server.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(trampoline()))
        .build()
        .create(RegionInfoApi.class);
  }

  @Test public void successfullyFetchRegionInfo() throws IOException {
    final MockResponse mockResponse = MockWebServerRule.createMockResponse("/regionInfo.json");
    serverRule.server.enqueue(mockResponse);

    final TestObserver<RegionInfoResponse> subscriber = api.fetchRegionInfoAsync(
        "/regionInfo.json",
        ImmutableRegionInfoBody.of("AU_NSW_Sydney")
    ).test();

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    final RegionInfoResponse response = subscriber.values().get(0);
    assertThat(response.regions()).hasSize(1);
    final RegionInfo regionInfo = response.regions().get(0);
    assertThat(regionInfo.transitWheelchairAccessibility()).isTrue();
  }
}