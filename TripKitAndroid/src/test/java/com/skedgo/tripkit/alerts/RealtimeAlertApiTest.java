package com.skedgo.tripkit.alerts;

import com.skedgo.tripkit.TripKitAndroidRobolectricTest;
import com.skedgo.tripkit.common.model.ImmutableRealtimeAlert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.reactivex.observers.TestObserver;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import thuytrinh.mockwebserverrule.MockWebServerRule;

import static io.reactivex.schedulers.Schedulers.trampoline;
import static thuytrinh.mockwebserverrule.MockWebServerRule.createMockResponse;

@RunWith(AndroidJUnit4.class)
public class RealtimeAlertApiTest extends TripKitAndroidRobolectricTest {
    @Rule
    public final MockWebServerRule serverRule = new MockWebServerRule();
    private RealtimeAlertApi api;

    @Before
    public void before() {
        api = new Retrofit.Builder()
            .baseUrl(serverRule.server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(trampoline()))
            .build()
            .create(RealtimeAlertApi.class);
    }

    @Test
    public void fetchRealtimeAlertsSuccessfully() throws IOException {
        serverRule.server.enqueue(createMockResponse("/alerts-transit.json"));
        final String regionName = "AU_NSW_Sydney";

        final TestObserver<RealtimeAlertResponse> subscriber = api.fetchRealtimeAlertsAsync(
            serverRule.server.url("/").toString(),
            regionName
        ).test();

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
                            .url("http://www.sydneytrains.info/service_updates/service_interruptions/")
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
                            .url("")
                            .build()
                    )
                    .build()
            ))
            .build();
        subscriber.assertValue(response);
    }
}