package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.util.Gsons;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TemporaryUrlApiTest {
  private MockWebServer server;
  private TemporaryUrlApi api;
  private HttpUrl baseUrl;

  @Before public void before() {
    server = new MockWebServer();
    baseUrl = server.url("/");
    api = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gsons.createForLowercaseEnum()))
        .build()
        .create(TemporaryUrlApi.class);
  }

  @After public void after() throws IOException {
    server.shutdown();
  }

  @Test public void fetchTripSuccessfully() throws IOException {
    final MockResponse mockResponse = new MockResponse();
    mockResponse.setResponseCode(200);
    mockResponse.setBody(IOUtils.toString(
        getClass().getResourceAsStream("/temporaryURL.json"),
        Charset.defaultCharset()
    ));
    server.enqueue(mockResponse);

    final HttpUrl url = baseUrl.newBuilder()
        .addPathSegments("trip/0a1cba21-f177-4706-bbb8-ebd8057e5f4f")
        .build();
    final TestSubscriber<RoutingResponse> subscriber = new TestSubscriber<>();
    api.requestTemporaryUrlAsync(
        url.toString(),
        Collections.<String, Object>emptyMap()
    ).subscribe(subscriber);

    final RoutingResponse response = subscriber.getOnNextEvents().get(0);
    assertThat(response.getTripGroupList()).isNotEmpty();
  }
}