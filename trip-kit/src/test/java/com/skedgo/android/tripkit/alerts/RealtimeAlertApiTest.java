package com.skedgo.android.tripkit.alerts;

import com.skedgo.android.common.model.ImmutableRealtimeAlert;
import com.skedgo.android.tripkit.BuildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.Arrays;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static rx.schedulers.Schedulers.immediate;
import static thuytrinh.mockwebserverrule.MockWebServerRule.createMockResponse;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class RealtimeAlertApiTest {
  @Rule public final MockWebServerRule serverRule = new MockWebServerRule();
  private RealtimeAlertApi api;

  @Before public void before() {
    api = new Retrofit.Builder()
        .baseUrl(serverRule.server.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(immediate()))
        .build()
        .create(RealtimeAlertApi.class);
  }

  @Test public void fetchRealtimeAlertsSuccessfully() throws IOException {
    serverRule.server.enqueue(createMockResponse("/alerts-transit.json"));

    final TestSubscriber<RealtimeAlertResponse> subscriber = new TestSubscriber<>();
    final String regionName = "AU_NSW_Sydney";
    api.fetchRealtimeAlertsAsync(
        serverRule.server.url("/").toString(),
        regionName
    ).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    final RealtimeAlertResponse response = ImmutableRealtimeAlertResponse.builder()
        .alerts(Arrays.<AlertBlock>asList(
            ImmutableAlertBlock.builder()
                .alert(
                    ImmutableRealtimeAlert.builder()
                        .remoteHashCode(707713596)
                        .severity("warning")
                        .title("Trackwork - Blue Mountains Line")
                        .text("Monday 29 August to Friday 2 September \n\n- Buses replace trains between Mount Victoria and Lithgow.\n- Trains to and from Bathurst run to a changed timetable.")
                        .build()
                )
                .build(),
            ImmutableAlertBlock.builder()
                .alert(
                    ImmutableRealtimeAlert.builder()
                        .remoteHashCode(697250695)
                        .severity("warning")
                        .title("Wharf Closed")
                        .text("McMahons Point Wharf Closed. Wharf closed for planned upgrade.")
                        .build()
                )
                .build()
        ))
        .build();
    subscriber.assertValue(response);
  }
}