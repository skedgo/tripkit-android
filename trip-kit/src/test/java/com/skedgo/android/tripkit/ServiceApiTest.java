package com.skedgo.android.tripkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.model.GsonAdaptersServiceShape;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.RealTimeVehicle;
import com.skedgo.android.common.model.RealtimeAlert;
import com.skedgo.android.common.model.ServiceShape;
import com.skedgo.android.common.model.Shape;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceApiTest {

  private ServiceApi serviceApi;
  private MockWebServer mockWebServer;

  @Before
  public void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    try {
      mockWebServer.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersTransitService())
        .registerTypeAdapterFactory(new GsonAdaptersServiceShape())
        .create();

    serviceApi = new Retrofit.Builder()
        .baseUrl(mockWebServer.url(""))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ServiceApi.class);
  }

  @Test public void getServiceAsync() throws IOException {

    mockWebServer.enqueue(new MockResponse().setBody(IOUtils.toString(getClass().
        getResourceAsStream("/service.json"), Charset.defaultCharset())));

    TestSubscriber<TransitService> subscriber = new TestSubscriber<>();
    serviceApi.getServiceAsync("", "", 0, true)
        .subscribe(subscriber);
    subscriber.awaitTerminalEvent();

    subscriber.assertNoErrors();
    TransitService transitService = subscriber.getOnNextEvents().get(0);

    assertThat(transitService.realTimeStatus()).isEqualTo("IS_REAL_TIME");
    assertThat(transitService.shapes()).hasSize(1);
    assertThat(transitService.realtimeAlternativeVehicle()).hasSize(1);

    ServiceShape shape = transitService.shapes().get(0);

    assertThat(shape.operator()).isEqualTo("Sydney Buses");
    assertThat(shape.serviceName()).isEqualTo("Epping");
    assertThat(shape.serviceNumber()).isEqualTo("290");
    assertThat(shape.serviceTripID()).isEqualTo("76396048_20160619_11954");

    RealTimeVehicle vehicle = transitService.realtimeVehicle();

    assertThat(vehicle.getLastUpdateTime()).isEqualTo(10);
    assertThat(vehicle.getLocation()).isNotNull();

    Location location = vehicle.getLocation();

    assertThat(location.getLat()).isEqualTo(20);
    assertThat(location.getLon()).isEqualTo(30);
    assertThat(location.getBearing()).isEqualTo(1);

  }
}